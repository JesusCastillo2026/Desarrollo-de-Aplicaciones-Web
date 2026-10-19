package com.hospital.quirofano.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.quirofano.dto.RolForm;
import com.hospital.quirofano.dto.UsuarioForm;
import com.hospital.quirofano.service.AuditoriaService;
import com.hospital.quirofano.service.RolService;
import com.hospital.quirofano.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/admin")
public class AdministracionWebController {
    private final UsuarioService usuarios;
    private final RolService roles;
    private final AuditoriaService auditoria;

    public AdministracionWebController(UsuarioService usuarios, RolService roles, AuditoriaService auditoria) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.auditoria = auditoria;
    }

    @GetMapping({"", "/usuarios"})
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarios.listarTodos().stream().map(com.hospital.quirofano.dto.UsuarioResponse::desde).toList());
        return "admin-usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("form", new UsuarioForm()); model.addAttribute("roles", roles.listarActivos());
        model.addAttribute("modoEdicion", false); return "admin-usuario-form";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        var usuario = usuarios.buscar(id); var form = new UsuarioForm();
        form.setNombreUsuario(usuario.getNombreUsuario()); form.setCorreo(usuario.getCorreo());
        form.setNombres(usuario.getNombres()); form.setApellidos(usuario.getApellidos());
        form.setRolId(usuario.getRol().getIdRol()); form.setActivo(usuario.isActivo());
        model.addAttribute("form", form); model.addAttribute("roles", roles.listarActivos());
        model.addAttribute("modoEdicion", true); model.addAttribute("id", id); return "admin-usuario-form";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam(required=false) Long id, @Valid @ModelAttribute("form") UsuarioForm form,
            BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roles.listarActivos()); model.addAttribute("modoEdicion", id != null);
            model.addAttribute("id", id); return "admin-usuario-form";
        }
        try { if (id == null) usuarios.crear(form); else usuarios.actualizar(id, form); flash.addFlashAttribute("mensaje", "Usuario guardado."); }
        catch (RuntimeException ex) { flash.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/web/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/estado")
    public String estadoUsuario(@PathVariable Long id, RedirectAttributes flash) {
        try { usuarios.cambiarEstado(id); } catch (RuntimeException ex) { flash.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/web/admin/usuarios";
    }

    @GetMapping("/roles")
    public String roles(Model model) { model.addAttribute("roles", roles.listarTodos()); return "admin-roles"; }

    @GetMapping("/roles/nuevo")
    public String nuevoRol(Model model) { model.addAttribute("form", new RolForm()); return "admin-rol-form"; }

    @GetMapping("/roles/{id}/editar")
    public String editarRol(@PathVariable Long id, Model model) {
        var rol = roles.buscar(id); var form = new RolForm(); form.setNombreRol(rol.getNombreRol());
        form.setDescripcion(rol.getDescripcion()); form.setActivo(rol.isActivo());
        model.addAttribute("form", form); model.addAttribute("id", id); return "admin-rol-form";
    }

    @PostMapping("/roles/guardar")
    public String guardarRol(@RequestParam(required=false) Long id, @Valid @ModelAttribute("form") RolForm form,
            BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) { model.addAttribute("id", id); return "admin-rol-form"; }
        try { if (id == null) roles.crear(form); else roles.actualizar(id, form); flash.addFlashAttribute("mensaje", "Rol guardado."); }
        catch (RuntimeException ex) { flash.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/web/admin/roles";
    }

    @PostMapping("/roles/{id}/estado")
    public String estadoRol(@PathVariable Long id, RedirectAttributes flash) {
        try { roles.cambiarEstado(id); } catch (RuntimeException ex) { flash.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/web/admin/roles";
    }

    @GetMapping("/auditoria")
    public String auditoria(Model model) { model.addAttribute("eventos", auditoria.listarRecientes()); return "admin-auditoria"; }
}
