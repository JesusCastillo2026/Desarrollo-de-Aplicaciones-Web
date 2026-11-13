package com.tecsup.producto.model;
import jakarta.persistence.*; import java.math.BigDecimal;
@Entity @Table(name="producto") public class Producto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=140) private String nombre;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal precio;
 @Column(nullable=false) private Integer stock;
 @Column(nullable=false) private Long categoriaId;
 public Producto(){} public Long getId(){return id;} public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;} public BigDecimal getPrecio(){return precio;} public void setPrecio(BigDecimal v){precio=v;} public Integer getStock(){return stock;} public void setStock(Integer v){stock=v;} public Long getCategoriaId(){return categoriaId;} public void setCategoriaId(Long v){categoriaId=v;}
}