# Semana 04 — Evaluación de Gestión Quirúrgica

## Objetivo

Desarrollar el módulo de Quirófano / Cirugía de un sistema hospitalario, gestionando el flujo desde la solicitud quirúrgica hasta la evaluación preoperatoria, la aprobación y la programación de la intervención.

## Cómo se logró

Se amplió la aplicación Spring Boot existente con dos procesos que complementan la solicitud quirúrgica:

- **Evaluación preoperatoria y aprobación:** registra al profesional responsable, antecedentes relevantes, alergias, medicación, observaciones y el estado de la evaluación. Las decisiones de aprobación, observación o rechazo conservan responsable, fecha, observación y motivo. Una decisión de rechazo requiere motivo.
- **Programación quirúrgica:** asigna quirófano, fecha, hora, duración, cirujano, anestesiólogo y equipo quirúrgico. Se validan las aprobaciones y la evaluación apta antes de programar; también se detectan cruces de horario para salas y profesionales. La reprogramación y cancelación guardan su motivo.
- **Gestión de quirófanos:** permite registrar y actualizar código, nombre, área, capacidad, equipamiento y estado para usarlos en la agenda.
- **Persistencia y presentación:** se incorporaron entidades y repositorios JPA, servicios para las reglas del proceso, endpoints REST y vistas Thymeleaf para registrar y consultar la información.

## Requerimientos implementados

1. **Solicitud quirúrgica:** RF-CIR-01 a RF-CIR-10; registro, relación con paciente e historia clínica, consulta y cancelación con motivo.
2. **Evaluación preoperatoria y aprobación:** RF-CIR-11 a RF-CIR-20; registro y consulta de evaluaciones, decisiones y responsable.
3. **Programación quirúrgica:** RF-CIR-21 a RF-CIR-32; asignaciones, disponibilidad, conflictos, reprogramación y cancelación.
4. **Gestión de quirófanos relacionada con la programación:** RF-CIR-33 a RF-CIR-37; alta, actualización, equipamiento, estado y disponibilidad.

## Flujo

```text
Solicitud quirúrgica
        ↓
Evaluación preoperatoria (APTA / OBSERVADA / NO_APTA)
        ↓
Aprobación (APROBADA / OBSERVADA / RECHAZADA)
        ↓
Programación de quirófano, horario y personal
        ↓
Reprogramación o cancelación con motivo cuando corresponda
```

## Herramientas

Java 17, Spring Boot, Spring Web MVC, Spring Data JPA con Hibernate, MySQL en XAMPP, Thymeleaf, HTML, Bootstrap y Maven. Postman puede utilizarse para verificar los endpoints REST.

## Proyecto

El código, instrucciones de ejecución, entidades, rutas REST y vistas están documentados en [Evaluación](EVALUACION/README.md) y en el [README de la aplicación](EVALUACION/quirofano-solicitudes-api/README.md).
