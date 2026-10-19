package com.tecsup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.dto.CategoriaDTO;
import com.tecsup.repository.AuditoriaRepository;
import com.tecsup.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Timeout(30)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categorias;

    @Autowired
    private AuditoriaRepository auditorias;

    @BeforeEach
    void limpiarBaseDePrueba() {
        auditorias.deleteAll();
        categorias.deleteAll();
    }

    @Test
    void postCreaCategoriaYDevuelve201ConLocation() throws Exception {
        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON)
                        .content(json("Cómputo", "Equipos y componentes")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Cómputo"));
    }

    @Test
    void getListaYConsultaCategoriaPorId() throws Exception {
        var creada = categorias.save(new com.tecsup.model.Categoria("Accesorios", "Periféricos"));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Accesorios"));
        mockMvc.perform(get("/api/categorias/{id}", creada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Periféricos"));
    }

    @Test
    void putActualizaCategoria() throws Exception {
        var creada = categorias.save(new com.tecsup.model.Categoria("Audio", "Sonido"));

        mockMvc.perform(put("/api/categorias/{id}", creada.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Audio y video", "Sonido e imagen")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Audio y video"));
    }

    @Test
    void deleteEliminaCategoriaYDevuelve204() throws Exception {
        var creada = categorias.save(new com.tecsup.model.Categoria("Varios", null));

        mockMvc.perform(delete("/api/categorias/{id}", creada.getId()))
                .andExpect(status().isNoContent());

        assertFalse(categorias.existsById(creada.getId()));
    }

    @Test
    void getCategoriaInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/categorias/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void postRechazaNombreEnBlanco() throws Exception {
        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON)
                        .content(json("   ", "Descripción")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    void postRechazaNombreDuplicadoIgnorandoMayusculas() throws Exception {
        categorias.save(new com.tecsup.model.Categoria("Accesorios", null));

        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON)
                        .content(json("ACCESORIOS", null)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    private String json(String nombre, String descripcion) throws Exception {
        return objectMapper.writeValueAsString(new CategoriaDTO(null, nombre, descripcion));
    }
}
