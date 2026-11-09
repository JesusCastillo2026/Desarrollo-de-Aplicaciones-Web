package com.tecsup.paciente.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record PacienteRequest(
        @NotBlank @Pattern(regexp="[0-9]{8}") String dni,
        @NotBlank @Size(max=100) String nombres,
        @NotBlank @Size(max=100) String apellidos,
        @Past java.time.LocalDate fechaNacimiento,
        @Pattern(regexp="[FM]") String sexo,
        @Pattern(regexp="[0-9+ -]{7,15}") String telefono,
        @Size(max=200) String direccion,
        @Email @Size(max=100) String correo,
        @NotBlank @Pattern(regexp="A|I") String estado
) {}
