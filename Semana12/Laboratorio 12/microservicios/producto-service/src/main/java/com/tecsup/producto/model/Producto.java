package com.tecsup.producto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tecsup.producto.dto.CategoriaResponse;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Long categoriaId;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoriaResponse categoria;

    public Producto() {}
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public CategoriaResponse getCategoria() { return categoria; }
    public void setCategoria(CategoriaResponse categoria) { this.categoria = categoria; }
}
