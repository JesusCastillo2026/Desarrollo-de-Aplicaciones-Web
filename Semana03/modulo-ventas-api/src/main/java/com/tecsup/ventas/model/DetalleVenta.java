package com.tecsup.ventas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    private Venta venta;
    @ManyToOne(optional = false) @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    @Column(nullable = false)
    private Integer cantidad;
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    protected DetalleVenta() {}
    public DetalleVenta(Venta venta, Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        this.venta=venta; this.producto=producto; this.cantidad=cantidad; this.precioUnitario=precioUnitario;
    }
    public Long getId(){return id;}
    public Venta getVenta(){return venta;} public void setVenta(Venta v){venta=v;}
    public Producto getProducto(){return producto;} public void setProducto(Producto v){producto=v;}
    public Integer getCantidad(){return cantidad;} public void setCantidad(Integer v){cantidad=v;}
    public BigDecimal getPrecioUnitario(){return precioUnitario;} public void setPrecioUnitario(BigDecimal v){precioUnitario=v;}
    public BigDecimal getSubtotal(){return precioUnitario.multiply(BigDecimal.valueOf(cantidad));}
}
