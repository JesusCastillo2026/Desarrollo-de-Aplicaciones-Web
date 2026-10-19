package com.tecsup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.model.Producto;
import com.tecsup.repository.AuditoriaRepository;
import com.tecsup.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Timeout(30)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository productos;

    @Autowired
    private AuditoriaRepository auditorias;

    @BeforeEach
    void limpiarBaseDePrueba() {
        auditorias.deleteAll();
        productos.deleteAll();
    }

    @Test
    void postCreaProductoYDevuelve201() throws Exception {
        var dto = productoDto("Mouse", 80, 20, "Accesorios");

        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Mouse"));
    }

    @Test
    void getListaProductos() throws Exception {
        productos.save(new Producto(null, "Monitor", 500, 10, "Pantallas"));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Monitor"));
    }

    @Test
    void postRechazaNombreVacio() throws Exception {
        var dto = productoDto("", 10, 2, "Varios");

        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    void putActualizaProductoExistente() throws Exception {
        Producto existente = productos.save(new Producto(null, "Teclado", 100, 2, "Accesorios"));

        mockMvc.perform(put("/api/productos/{id}", existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDto("Teclado mecánico", 150, 5, "Accesorios"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado mecánico"))
                .andExpect(jsonPath("$.precio").value(150));
    }

    @Test
    void putDevuelve404SiElProductoNoExiste() throws Exception {
        mockMvc.perform(put("/api/productos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDto("Mouse", 80, 1, "Accesorios"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEliminaProductoExistente() throws Exception {
        Producto existente = productos.save(new Producto(null, "Mouse", 80, 20, "Accesorios"));

        mockMvc.perform(delete("/api/productos/{id}", existente.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado correctamente"));

        org.junit.jupiter.api.Assertions.assertFalse(productos.existsById(existente.getId()));
    }

    @Test
    void deleteDevuelve404SiElProductoNoExiste() throws Exception {
        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    private ProductoDTOTestData productoDto(String nombre, double precio, int stock, String categoria) {
        return new ProductoDTOTestData(nombre, precio, stock, categoria);
    }

    private record ProductoDTOTestData(String nombre, double precio, int stock, String categoria) { }
}
