# Sistema de Gestión Quirúrgica - Módulo de Quirófano

Proyecto desarrollado con Spring Boot para la sesión de Construcción de Servicios Web RESTful e Interfaces Web MVC. Implementa una arquitectura en capas, persistencia relacional con MySQL y vistas dinámicas con Thymeleaf.

## Requerimiento Implementado
* **Requerimiento 5: Solicitud Quirúrgica**
  * Registro de solicitudes originadas desde Consulta Médica, Hospitalización o Emergencia.
  * Relación con Pacientes, Médicos e Historia Clínica.
  * Control de estados: `SOLICITADA`, `CANCELADA`, entre otros.
  * Niveles de prioridad y campos clínicos (diagnóstico, motivo y observaciones).

## Arquitectura y Tecnologías
* **Backend:** Java, Spring Boot, Spring Data JPA, Spring Web.
* **Inversión de Control (IoC):** Inyección de dependencias implementada por constructor en servicios y controladores.
* **Base de Datos:** MySQL (XAMPP) con mapeo relacional mediante `@ManyToOne`.
* **Frontend:** Thymeleaf, HTML5, Bootstrap y animaciones CSS con temática hospitalaria.
* **Pruebas:** Endpoints probados y documentados en Postman.

## Estructura del Proyecto
* `controller/`: Controladores REST (`SolicitudCirugiaController`) y MVC (`SolicitudWebController`).
* `service/`: Lógica de negocio y reglas operativas (`SolicitudCirugiaService`).
* `repository/`: Interfaces de acceso a datos con Spring Data JPA.
* `entity/`: Modelos de dominio (`SolicitudCirugia`, `Paciente`, `Medico`, `HistoriaClinica`).
* `templates/`: Vistas de usuario (`home.html`, `formulario-solicitud.html`, `lista-solicitudes.html`).

## Endpoints Principales
* `GET /api/solicitudes`: Lista de solicitudes registradas.
* `POST /api/solicitudes`: Registro de nueva solicitud vía JSON.
* `PUT /api/solicitudes/{id}/cancelar?motivo=...`: Cancelación controlada con motivo.
* `DELETE /api/solicitudes/{id}`: Eliminación del registro.

## Vistas Web
* `GET /web/solicitudes/home`: Panel principal.
* `GET /web/solicitudes`: Bandeja de gestión con acciones de cancelación y eliminación.
* `GET /web/solicitudes/nueva`: Formulario de programación quirúrgica.