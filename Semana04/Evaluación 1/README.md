# Evaluación — Sistema de Gestión Quirúrgica

## Objetivo

Completar el módulo de Quirófano / Cirugía del sistema hospitalario con tres requerimientos conectados: solicitud quirúrgica, evaluación preoperatoria con aprobación y programación quirúrgica.

## Cómo se logró

La aplicación mantiene arquitectura por capas con controladores, servicios, repositorios y entidades JPA. La solicitud existente se amplió con registros de evaluación preoperatoria, decisiones de aprobación, programación, quirófanos e integrantes del equipo.

Las reglas del servicio exigen una evaluación APTA y una decisión APROBADA para programar. La agenda verifica que el quirófano, el cirujano, el anestesiólogo y el personal incluido no tengan otra intervención en el mismo intervalo. La reprogramación y cancelación conservan el motivo. Cuando el servidor entrega una identidad autenticada, se registra automáticamente como responsable; en esta versión sin inicio de sesión, el responsable se indica en el formulario o la petición. Las pantallas Thymeleaf permiten administrar solicitudes, evaluaciones, decisiones, quirófanos y programaciones.

## Requerimientos

- **Solicitud quirúrgica (RF-CIR-01 a RF-CIR-10):** registro y consulta de solicitudes, vínculos con paciente, médico e historia clínica, prioridad, estado y cancelación con motivo.
- **Evaluación preoperatoria (RF-CIR-11 a RF-CIR-15):** registro profesional de antecedentes, alergias, medicación, observaciones y estado; consulta del historial por solicitud o paciente.
- **Aprobación (RF-CIR-16 a RF-CIR-20):** guardar aprobación, rechazo u observación con responsable y fecha; exigir motivo al rechazar; bloquear programación sin aprobación válida.
- **Programación (RF-CIR-21 a RF-CIR-32):** asignar sala y personal, fijar inicio y duración, calcular fin estimado, evitar conflictos y permitir reprogramar o cancelar con motivo.
- **Quirófanos (RF-CIR-33 a RF-CIR-37):** registrar y modificar sala, equipamiento y estado; consultar salas habilitadas para programación.

## Flujo

```text
Solicitud SOLICITADA
  → Evaluación preoperatoria
  → Decisión APROBADA
  → Programación con sala y equipo disponibles
  → Reprogramación o cancelación documentada si es necesario
```

## Herramientas

Java 17, Spring Boot, Spring Web MVC, Spring Data JPA/Hibernate, MySQL con XAMPP, Thymeleaf, Bootstrap, Maven, Postman y Visual Studio Code.

## Ejecución

1. Inicia MySQL desde XAMPP y crea la base `hospital_db`.
2. Revisa usuario y contraseña de MySQL en `src/main/resources/application.properties`.
3. Desde esta carpeta ejecuta `mvnw.cmd spring-boot:run` en Windows.
4. Abre `http://localhost:8080/web/solicitudes/home`.

Hibernate actualiza las tablas del esquema según las entidades (`spring.jpa.hibernate.ddl-auto=update`). Las solicitudes y relaciones originales se conservan.

## Endpoints principales

| Método y ruta | Función |
|---|---|
| `GET /api/solicitudes` | Lista solicitudes; admite `?estado=...`. |
| `POST /api/solicitudes` | Registra una solicitud quirúrgica. |
| `PUT /api/solicitudes/{id}` | Actualiza una solicitud. |
| `PUT /api/solicitudes/{id}/cancelar?motivo=...` | Cancela una solicitud con motivo. |
| `GET /api/evaluaciones-preoperatorias?solicitudId={id}` | Consulta evaluaciones por solicitud. Admite también `pacienteId`. |
| `POST /api/evaluaciones-preoperatorias` | Registra evaluación preoperatoria. |
| `PUT /api/evaluaciones-preoperatorias/{id}` | Actualiza una evaluación existente. |
| `GET /api/solicitudes/{id}/aprobaciones` | Consulta decisiones de la solicitud. |
| `POST /api/solicitudes/{id}/aprobaciones` | Registra aprobación, observación o rechazo. |
| `GET, POST /api/quirofanos` | Lista y registra quirófanos. |
| `GET, PUT /api/quirofanos/{id}` | Consulta o actualiza quirófano y estado. |
| `GET /api/quirofanos/disponibles?inicio=2026-11-20T09:30:00&duracionMinutos=90` | Consulta salas disponibles para ese intervalo. |
| `GET /api/programaciones?solicitudId={id}&desde={fecha}&hasta={fecha}&quirofanoId={id}&cirujanoId={id}` | Consulta y filtra la agenda; todos los filtros son opcionales. |
| `POST /api/programaciones` | Programa una cirugía aprobada. |
| `PUT /api/programaciones/{id}/reprogramar` | Reprograma con motivo obligatorio y revalida disponibilidad. |
| `PUT /api/programaciones/{id}/cancelar` | Cancela con responsable y motivo. |
| `GET /api/programaciones/{id}/equipo` | Consulta integrantes asignados. |

Los identificadores de solicitud, médico, quirófano y demás referencias deben existir. El formato de fecha y hora de programación es `yyyy-MM-ddTHH:mm:ss`, por ejemplo `2026-11-20T09:30:00`.

### Ejemplo: evaluación

```json
{
  "solicitudId": 1,
  "profesionalId": 1,
  "antecedentesRelevantes": "Antecedentes revisados por el profesional",
  "alergias": "Ninguna reportada",
  "medicacionActual": "Según historia clínica",
  "observaciones": "Evaluación completada",
  "estado": "APTA"
}
```

### Ejemplo: decisión

```json
{
  "estado": "APROBADA",
  "responsable": "Profesional responsable",
  "observacion": "Se autoriza continuar con la programación",
  "motivo": ""
}
```

### Ejemplo: programación

```json
{
  "solicitudId": 1,
  "quirofanoId": 1,
  "cirujanoId": 1,
  "anestesiologoId": 2,
  "fechaHoraInicio": "2026-11-20T09:30:00",
  "duracionEstimadaMinutos": 90,
  "prioridad": "URGENTE",
  "motivoCambio": "",
  "equipo": [
    { "medicoId": 3, "funcion": "Enfermería instrumentista" }
  ]
}
```

## Vistas web

- `GET /web/solicitudes/home`: panel del sistema.
- `GET /web/solicitudes`: bandeja de solicitudes y acceso a evaluación/programación.
- `GET /web/solicitudes/{id}/evaluacion`: captura de evaluación y decisiones con sus historiales.
- `GET /web/solicitudes/{id}/programar`: formulario de programación.
- `GET /web/solicitudes/programaciones`: agenda con reprogramación y cancelación.
- `GET /web/solicitudes/quirofanos`: administración de quirófanos.

## Código fuente

La aplicación se encuentra en [quirofano-solicitudes-api](quirofano-solicitudes-api/README.md).
