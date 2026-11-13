package com.tecsup.categoria.service;
import com.tecsup.categoria.dto.CategoriaRequest;
import com.tecsup.categoria.model.Categoria;
import com.tecsup.categoria.repository.CategoriaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class CategoriaService {
  private final CategoriaRepository repository;
  public CategoriaService(CategoriaRepository repository) { this.repository=repository; }
  public List<Categoria> listar() { return repository.findAll(); }
  public Categoria obtener(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No existe el recurso " + id)); }
  public Categoria crear(CategoriaRequest dto) { Categoria e=new Categoria(); aplicar(e,dto); return repository.save(e); }
  public Categoria actualizar(Long id,CategoriaRequest dto) { Categoria e=obtener(id); aplicar(e,dto); return repository.save(e); }
  private void aplicar(Categoria e,CategoriaRequest d) {
    e.setNombre(d.nombre());
    e.setEstado(d.estado());
  }
  public void eliminar(Long id) { repository.delete(obtener(id)); }
}
