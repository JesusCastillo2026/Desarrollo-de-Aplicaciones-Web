package com.tecsup.categoria.repository;
import com.tecsup.categoria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {}
