package com.tecsup.ventas.service;

import com.tecsup.ventas.dto.DetalleVentaRequest;
import com.tecsup.ventas.model.DetalleVenta;
import com.tecsup.ventas.model.Producto;
import com.tecsup.ventas.model.Venta;
import com.tecsup.ventas.repository.DetalleVentaRepository;
import com.tecsup.ventas.repository.ProductoRepository;
import com.tecsup.ventas.repository.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DetalleVentaService {
    private final DetalleVentaRepository detalles;
    private final VentaRepository ventas;
    private final ProductoRepository productos;
    public DetalleVentaService(DetalleVentaRepository detalles, VentaRepository ventas, ProductoRepository productos) {
        this.detalles=detalles; this.ventas=ventas; this.productos=productos;
    }
    public List<DetalleVenta> listar(Long ventaId) { obtenerVenta(ventaId); return detalles.findByVentaId(ventaId); }
    public DetalleVenta obtener(Long ventaId, Long detalleId) { return detalles.findByVentaId(ventaId).stream().filter(d -> d.getId().equals(detalleId)).findFirst().orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado en la venta " + ventaId)); }
    @Transactional public DetalleVenta crear(Long ventaId, DetalleVentaRequest r) {
        Venta venta=obtenerVenta(ventaId); Producto producto=producto(r.productoId()); descontar(producto, r.cantidad());
        DetalleVenta detalle=new DetalleVenta(venta, producto, r.cantidad(), producto.getPrecio());
        venta.getDetalles().add(detalle); ventas.save(venta); return detalle;
    }
    @Transactional public DetalleVenta actualizar(Long ventaId, Long detalleId, DetalleVentaRequest r) {
        DetalleVenta detalle=obtener(ventaId, detalleId); Producto anterior=detalle.getProducto();
        anterior.setStock(anterior.getStock()+detalle.getCantidad());
        Producto nuevo=producto(r.productoId()); descontar(nuevo, r.cantidad());
        detalle.setProducto(nuevo); detalle.setCantidad(r.cantidad()); detalle.setPrecioUnitario(nuevo.getPrecio());
        return detalle;
    }
    @Transactional public void eliminar(Long ventaId, Long detalleId) {
        DetalleVenta detalle=obtener(ventaId, detalleId); Producto producto=detalle.getProducto();
        producto.setStock(producto.getStock()+detalle.getCantidad());
        Venta venta=detalle.getVenta(); venta.getDetalles().remove(detalle); detalles.delete(detalle);
    }
    private Venta obtenerVenta(Long id) { return ventas.findById(id).orElseThrow(() -> new EntityNotFoundException("Venta " + id + " no encontrada")); }
    private Producto producto(Long id) { return productos.findById(id).orElseThrow(() -> new EntityNotFoundException("Producto " + id + " no encontrado")); }
    private void descontar(Producto producto, int cantidad) { if(producto.getStock()<cantidad) throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre()); producto.setStock(producto.getStock()-cantidad); }
}
