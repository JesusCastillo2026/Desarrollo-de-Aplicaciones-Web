package com.tecsup.pedido.service;

import com.tecsup.pedido.client.ClienteClient;
import com.tecsup.pedido.client.ProductoClient;
import com.tecsup.pedido.dto.ClienteResponse;
import com.tecsup.pedido.dto.EstadoRequest;
import com.tecsup.pedido.dto.PedidoRequest;
import com.tecsup.pedido.dto.PedidoResponse;
import com.tecsup.pedido.dto.ProductoResponse;
import com.tecsup.pedido.model.DetallePedido;
import com.tecsup.pedido.model.Pedido;
import com.tecsup.pedido.repository.PedidoRepository;
import feign.FeignException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    private final ClienteClient clienteClient;
    private final ProductoClient productoClient;

    public PedidoService(PedidoRepository repo, ClienteClient clienteClient, ProductoClient productoClient) {
        this.repo = repo;
        this.clienteClient = clienteClient;
        this.productoClient = productoClient;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return repo.findAll().stream().map(this::armarRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtener(Long id) {
        return armarRespuesta(buscar(id));
    }

    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        validarCliente(request.idCliente());
        Pedido pedido = construirPedido(request);
        return armarRespuesta(repo.save(pedido));
    }

    @Transactional
    public PedidoResponse actualizar(Long id, PedidoRequest request) {
        Pedido actual = buscar(id);
        validarCliente(request.idCliente());
        Pedido nuevo = construirPedido(request);
        actual.setFecha(nuevo.getFecha());
        actual.setIdCliente(nuevo.getIdCliente());
        actual.setTotal(nuevo.getTotal());
        actual.getDetalles().clear();
        for (DetallePedido detalle : nuevo.getDetalles()) {
            detalle.setPedido(actual);
            actual.getDetalles().add(detalle);
        }
        return armarRespuesta(repo.save(actual));
    }

    @Transactional
    public PedidoResponse cambiarEstado(Long id, EstadoRequest request) {
        Pedido pedido = buscar(id);
        pedido.setEstado(request.estado());
        return armarRespuesta(repo.save(pedido));
    }

    private Pedido buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    private Pedido construirPedido(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setFecha(request.fecha());
        pedido.setIdCliente(request.idCliente());
        pedido.setEstado("PENDIENTE");

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();
        for (PedidoRequest.DetalleRequest linea : request.detalles()) {
            ProductoResponse producto = validarProducto(linea.idProducto());
            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(producto.id());
            detalle.setCantidad(linea.cantidad());
            detalle.setPedido(pedido);
            detalles.add(detalle);
            total = total.add(producto.precio().multiply(BigDecimal.valueOf(linea.cantidad())));
        }
        pedido.setDetalles(detalles);
        pedido.setTotal(total);
        return pedido;
    }

    private PedidoResponse armarRespuesta(Pedido pedido) {
        ClienteResponse cliente = llamarCliente(pedido.getIdCliente());
        List<PedidoResponse.DetalleResponse> detalles = pedido.getDetalles().stream()
                .map(detalle -> new PedidoResponse.DetalleResponse(
                        detalle.getId(), detalle.getCantidad(), llamarProducto(detalle.getIdProducto())))
                .toList();
        return new PedidoResponse(pedido.getId(), pedido.getFecha(), pedido.getEstado(), pedido.getTotal(), cliente, detalles);
    }

    private ClienteResponse validarCliente(Long id) { return llamarCliente(id); }
    private ProductoResponse validarProducto(Long id) { return llamarProducto(id); }

    private ClienteResponse llamarCliente(Long id) {
        try {
            return clienteClient.obtenerCliente(id);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente " + id + " no existe");
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "El servicio de clientes no está disponible");
        }
    }

    private ProductoResponse llamarProducto(Long id) {
        try {
            return productoClient.obtenerProducto(id);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto " + id + " no existe");
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "El servicio de productos no está disponible");
        }
    }
}
