package com.tecsup.restaurante.pedido.services;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import com.tecsup.restaurante.pedido.dto.ClienteRemotoResponse;
import com.tecsup.restaurante.pedido.dto.DetallePedidoRequest;
import com.tecsup.restaurante.pedido.dto.PedidoRequest;
import com.tecsup.restaurante.pedido.dto.PedidoResponse;
import com.tecsup.restaurante.pedido.dto.PlatoRemotoResponse;
import com.tecsup.restaurante.pedido.model.DetallePedido;
import com.tecsup.restaurante.pedido.model.Pedido;
import com.tecsup.restaurante.pedido.repository.PedidoRepository;

@Service
@Transactional
public class PedidoService {
    private static final Set<String> ESTADOS = Set.of("REGISTRADO", "EN_PREPARACION", "LISTO", "ENTREGADO", "CANCELADO");

    private final PedidoRepository repository;
    private final RestClient clienteRestClient;
    private final RestClient platoRestClient;

    public PedidoService(PedidoRepository repository, RestClient clienteRestClient, RestClient platoRestClient) {
        this.repository = repository;
        this.clienteRestClient = clienteRestClient;
        this.platoRestClient = platoRestClient;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return repository.findAll().stream().map(PedidoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id) { return PedidoResponse.from(obtener(id)); }

    public PedidoResponse crear(PedidoRequest request) {
        verificarCliente(request.idCliente());
        Pedido pedido = new Pedido();
        pedido.setIdCliente(request.idCliente());
        pedido.setEstado(normalizarEstado(request.estado(), "REGISTRADO"));
        actualizarDetalles(pedido, request.detalles());
        return PedidoResponse.from(repository.save(pedido));
    }

    public PedidoResponse actualizar(Long id, PedidoRequest request) {
        Pedido pedido = obtener(id);
        verificarCliente(request.idCliente());
        pedido.setIdCliente(request.idCliente());
        pedido.setEstado(normalizarEstado(request.estado(), pedido.getEstado()));
        actualizarDetalles(pedido, request.detalles());
        return PedidoResponse.from(repository.save(pedido));
    }

    public void eliminar(Long id) { repository.delete(obtener(id)); }

    private void actualizarDetalles(Pedido pedido, List<DetallePedidoRequest> solicitudes) {
        Set<Long> platosUnicos = new HashSet<>();
        pedido.getDetalles().clear();
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedidoRequest solicitud : solicitudes) {
            if (!platosUnicos.add(solicitud.idPlato())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada plato debe aparecer una sola vez; ajuste su cantidad en el detalle.");
            }
            PlatoRemotoResponse plato = obtenerPlato(solicitud.idPlato());
            if (!plato.disponible()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El plato " + plato.nombre() + " no está disponible.");
            }
            BigDecimal subtotal = plato.precio().multiply(BigDecimal.valueOf(solicitud.cantidad()));
            DetallePedido detalle = new DetallePedido();
            detalle.setIdPlato(plato.idPlato());
            detalle.setNombrePlato(plato.nombre());
            detalle.setCantidad(solicitud.cantidad());
            detalle.setPrecioUnitario(plato.precio());
            detalle.setSubtotal(subtotal);
            pedido.addDetalle(detalle);
            total = total.add(subtotal);
        }
        pedido.setTotal(total);
    }

    private void verificarCliente(Long idCliente) {
        try {
            ClienteRemotoResponse cliente = clienteRestClient.get().uri("/api/clientes/{id}", idCliente)
                    .retrieve().body(ClienteRemotoResponse.class);
            if (cliente == null) throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Cliente devolvió una respuesta vacía.");
            if (!cliente.activo()) throw new ResponseStatusException(HttpStatus.CONFLICT, "El cliente está inactivo.");
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No existe el cliente " + idCliente + ".");
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo consultar el servicio Cliente.");
        }
    }

    private PlatoRemotoResponse obtenerPlato(Long idPlato) {
        try {
            PlatoRemotoResponse plato = platoRestClient.get().uri("/api/platos/{id}", idPlato)
                    .retrieve().body(PlatoRemotoResponse.class);
            if (plato == null) throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Plato devolvió una respuesta vacía.");
            return plato;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No existe el plato " + idPlato + ".");
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo consultar el servicio Plato.");
        }
    }

    private Pedido obtener(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el pedido " + id + "."));
    }

    private String normalizarEstado(String estado, String valorPorDefecto) {
        if (estado == null || estado.isBlank()) return valorPorDefecto;
        String normalizado = estado.trim().toUpperCase(Locale.ROOT);
        if (!ESTADOS.contains(normalizado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido. Use REGISTRADO, EN_PREPARACION, LISTO, ENTREGADO o CANCELADO.");
        }
        return normalizado;
    }
}
