package com.tecsup.ventas.service;

import com.tecsup.ventas.dto.DetalleVentaRequest;
import com.tecsup.ventas.dto.VentaRequest;
import com.tecsup.ventas.model.*;
import com.tecsup.ventas.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventas;
    private final ClienteRepository clientes;
    private final EmpleadoRepository empleados;
    private final ProductoRepository productos;
    public VentaService(VentaRepository ventas, ClienteRepository clientes, EmpleadoRepository empleados, ProductoRepository productos) {
        this.ventas=ventas; this.clientes=clientes; this.empleados=empleados; this.productos=productos;
    }
    public List<Venta> listar() { return ventas.findAll(); }
    public Venta obtener(Long id) { return ventas.findById(id).orElseThrow(() -> new EntityNotFoundException("Venta " + id + " no encontrada")); }

    @Transactional public Venta crear(VentaRequest r) {
        Venta venta=new Venta(cliente(r.clienteId()), empleado(r.empleadoId()), estado(r.estado()));
        agregarDetalles(venta, r.detalles());
        return ventas.save(venta);
    }

    @Transactional public Venta actualizar(Long id, VentaRequest r) {
        Venta venta=obtener(id);
        reponerStock(venta.getDetalles());
        venta.getDetalles().clear();
        venta.setCliente(cliente(r.clienteId())); venta.setEmpleado(empleado(r.empleadoId()));
        if (r.estado()!=null) venta.setEstado(estado(r.estado()));
        agregarDetalles(venta, r.detalles());
        return ventas.save(venta);
    }

    @Transactional public void eliminar(Long id) {
        Venta venta=obtener(id); reponerStock(venta.getDetalles()); ventas.delete(venta);
    }

    private void agregarDetalles(Venta venta, List<DetalleVentaRequest> solicitudes) {
        for (DetalleVentaRequest solicitud : solicitudes) {
            Producto producto=productos.findById(solicitud.productoId()).orElseThrow(() -> new EntityNotFoundException("Producto " + solicitud.productoId() + " no encontrado"));
            if (producto.getStock()<solicitud.cantidad()) throw new StockInsuficienteException("Stock insuficiente para el producto " + producto.getNombre());
            producto.setStock(producto.getStock()-solicitud.cantidad());
            venta.getDetalles().add(new DetalleVenta(venta, producto, solicitud.cantidad(), producto.getPrecio()));
        }
    }

    private void reponerStock(List<DetalleVenta> detalles) {
        for (DetalleVenta detalle : detalles) {
            Producto producto=detalle.getProducto(); producto.setStock(producto.getStock()+detalle.getCantidad());
        }
    }
    private Cliente cliente(Long id) { return clientes.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente " + id + " no encontrado")); }
    private Empleado empleado(Long id) { return empleados.findById(id).orElseThrow(() -> new EntityNotFoundException("Empleado " + id + " no encontrado")); }
    private EstadoVenta estado(String valor) { return valor==null ? EstadoVenta.REGISTRADA : EstadoVenta.valueOf(valor); }
}
