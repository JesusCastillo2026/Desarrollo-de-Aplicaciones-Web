# Sistema de Gestión Quirúrgica — Módulo de Quirófano

Aplicación desarrollada con Spring Boot, Hibernate/JPA, MySQL y Thymeleaf. La evaluación integra solicitudes quirúrgicas, evaluación y aprobación preoperatoria y programación de cirugías.

## Objetivo

Gestionar el proceso quirúrgico desde la solicitud del procedimiento hasta su evaluación, aprobación y asignación a un quirófano y equipo disponibles.

## Cómo se logró

El sistema se organiza en capas `controller`, `service`, `repository` y `entity`. Los controladores exponen operaciones REST y páginas Thymeleaf; los servicios validan el flujo y las reglas de disponibilidad; Hibernate persiste las entidades y sus claves foráneas en MySQL.

Una solicitud se puede evaluar con estado `APTA`, `OBSERVADA` o `NO_APTA`. La decisión admite `APROBADA`, `OBSERVADA` o `RECHAZADA`. La programación requiere que la evaluación más reciente sea `APTA` y la decisión más reciente `APROBADA`. El sistema calcula la hora estimada de término y rechaza cruces de horario entre quirófano, cirujano, anestesiólogo o integrantes del equipo. La reprogramación y cancelación exigen motivo; las decisiones anteriores se conservan como historial.

## Requerimientos funcionales

- **Solicitud quirúrgica — RF-CIR-01 a RF-CIR-10:** registro y consulta, asociación a paciente, médico e historia clínica, prioridad y cancelación con motivo.
- **Evaluación preoperatoria — RF-CIR-11 a RF-CIR-15:** registro por profesional, observaciones clínicas, estado y consulta del historial.
- **Aprobación — RF-CIR-16 a RF-CIR-20:** decisiones con responsable, fecha, observación y motivo; no permite programar sin aprobación.
- **Programación — RF-CIR-21 a RF-CIR-32:** quirófano, fecha, hora, duración, cirujano, anestesiólogo y equipo; control de disponibilidad, reprogramación y cancelación.
- **Quirófanos — RF-CIR-33 a RF-CIR-37:** registrar y actualizar sala, equipamiento y estado.

## Flujo

```text
Solicitud quirúrgica
  → Evaluación preoperatoria
  → Aprobación / observación / rechazo
  → Programación de quirófano y equipo
  → Reprogramación o cancelación documentada
```

## Herramientas

Java 17, Spring Boot, Spring Web MVC, Spring Data JPA/Hibernate, MySQL, Thymeleaf, Bootstrap, Maven, Visual Studio Code y Postman.

## Base de datos y ejecución

1. Inicia Apache (si lo necesitas para phpMyAdmin) y MySQL en XAMPP.
2. Crea la base de datos `hospital_db` en phpMyAdmin.
3. Comprueba la URL, usuario y contraseña en `src/main/resources/application.properties`.
4. En Windows, abre una terminal en esta carpeta y ejecuta `mvnw.cmd spring-boot:run`.
5. Visita `http://localhost:8080/web/solicitudes/home`.

Hibernate actualiza el esquema existente con `spring.jpa.hibernate.ddl-auto=update`. Se agregarán las tablas de evaluación, aprobación, quirófanos, programación y equipo, manteniendo las tablas de la solicitud.

## API REST

Las solicitudes existentes están bajo `/api/solicitudes`. Las rutas nuevas son:

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/evaluaciones-preoperatorias?solicitudId=1` | Listar evaluaciones por solicitud. Acepta `pacienteId`. |
| GET | `/api/evaluaciones-preoperatorias/{id}` | Consultar una evaluación. |
| POST | `/api/evaluaciones-preoperatorias` | Registrar evaluación. |
| PUT | `/api/evaluaciones-preoperatorias/{id}` | Actualizar evaluación. |
| GET / POST | `/api/solicitudes/{id}/aprobaciones` | Consultar el historial o registrar una decisión. |
| GET / POST | `/api/quirofanos` | Listar o registrar quirófanos. |
| GET / PUT | `/api/quirofanos/{id}` | Consultar o actualizar quirófano y estado. |
| GET | `/api/quirofanos/disponibles?inicio=2026-11-20T09:30:00&duracionMinutos=90` | Consultar salas libres en un intervalo. |
| GET | `/api/programaciones?solicitudId=1&desde=2026-11-20T00:00:00&hasta=2026-11-21T00:00:00&quirofanoId=1&cirujanoId=1` | Consultar y filtrar la agenda; los filtros son opcionales. |
| GET | `/api/programaciones/{id}` | Consultar una programación. |
| POST | `/api/programaciones` | Programar una solicitud aprobada. |
| PUT | `/api/programaciones/{id}/reprogramar` | Cambiar fecha, hora o recursos con motivo obligatorio. |
| PUT | `/api/programaciones/{id}/cancelar` | Cancelar con responsable y motivo. |
| GET | `/api/programaciones/{id}/equipo` | Consultar el equipo asignado. |

Los errores de datos devuelven estados HTTP `400` (solicitud inválida), `404` (referencia inexistente) o `409` (conflicto de disponibilidad o de flujo).

### Ejemplo de evaluación — `POST /api/evaluaciones-preoperatorias`

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

### Ejemplo de aprobación — `POST /api/solicitudes/1/aprobaciones`

```json
{
  "estado": "APROBADA",
  "responsable": "Profesional responsable",
  "observacion": "Se autoriza continuar con la programación",
  "motivo": ""
}
```

### Ejemplo de programación — `POST /api/programaciones`

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
  "responsableCambio": "Usuario responsable",
  "equipo": [
    { "medicoId": 3, "funcion": "Enfermería instrumentista" }
  ]
}
```

## Interfaz web

- `/web/solicitudes/home`: accesos principales.
- `/web/solicitudes`: bandeja y acciones del proceso.
- `/web/solicitudes/{id}/evaluacion`: evaluación y decisiones.
- `/web/solicitudes/{id}/programar`: programación nueva.
- `/web/solicitudes/programaciones`: agenda, reprogramación y cancelación.
- `/web/solicitudes/quirofanos`: administración de salas y estado.

## Estructura relevante

- `entity/`: solicitudes, pacientes, médicos, historia clínica, evaluaciones, aprobaciones, quirófanos, programaciones e integrantes.
- `repository/`: persistencia y consultas Spring Data JPA.
- `service/`: reglas de transición, validación y disponibilidad.
- `controller/`: endpoints REST y controladores de las vistas.
- `resources/templates/`: formularios, bandejas, agenda y administración de quirófanos.
