package com.tecsup.ventas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private EstadoVenta estado = EstadoVenta.REGISTRADA;
    @ManyToOne(optional = false) @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    @ManyToOne(optional = false) @JoinColumn(name = "empleado_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empleado empleado;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    protected Venta() {}
    public Venta(Cliente cliente, Empleado empleado, EstadoVenta estado) {
        this.fecha = LocalDateTime.now(); this.cliente = cliente; this.empleado = empleado;
        this.estado = estado == null ? EstadoVenta.REGISTRADA : estado;
    }
    public Long getId(){return id;}
    public LocalDateTime getFecha(){return fecha;}
    public EstadoVenta getEstado(){return estado;} public void setEstado(EstadoVenta v){estado=v;}
    public Cliente getCliente(){return cliente;} public void setCliente(Cliente v){cliente=v;}
    public Empleado getEmpleado(){return empleado;} public void setEmpleado(Empleado v){empleado=v;}
    public List<DetalleVenta> getDetalles(){return detalles;}
    public BigDecimal getTotal(){return detalles.stream().map(DetalleVenta::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);}
}
