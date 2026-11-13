package com.tecsup.producto.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record ProductoRequest(@NotBlank @Size(max=140) String nombre,@NotNull @DecimalMin("0.01") BigDecimal precio,@NotNull @PositiveOrZero Integer stock,@NotNull @Positive Long categoriaId){}