# Apli01 — Información personal

Aplicación Spring Boot del Laboratorio 1. Presenta los datos personales del estudiante en una página Thymeleaf y ofrece el mismo perfil como JSON mediante una API REST.

## Requisitos

- JDK 17 o superior.
- Visual Studio Code con las extensiones de Java y Spring Boot.
- No requiere base de datos.

## Ejecución en Visual Studio Code

1. Abrir esta carpeta (`Semana01/Apli01`) en Visual Studio Code.
2. Abrir una terminal integrada en la carpeta del proyecto.
3. Ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

4. Esperar el mensaje `Started Apli01Application` y abrir `http://localhost:8080/`.

La ruta principal redirige al perfil. Para detener el servidor, presionar `Ctrl+C` en la terminal.

## Rutas

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/` | Redirige a la vista del perfil. |
| GET | `/mi-perfil` | Renderiza el perfil con Thymeleaf usando datos enviados mediante `Model`. |
| GET | `/api/perfil` | Devuelve el perfil como JSON. |

## Estructura principal

- `PerfilController`: atiende las rutas web y entrega el objeto `Persona` a la vista mediante `Model`.
- `PerfilRestController`: expone la ruta JSON con `@RestController` y el prefijo `/api` con `@RequestMapping`.
- `Persona`: contiene los datos del perfil y la lista de pasatiempos.
- `templates/perfil.html`: muestra los datos con Thymeleaf.
- `application.properties`: configura el nombre y el puerto local `8080`.

## Contenido implementado

Se aplican controladores MVC, una vista HTML dinámica, el objeto `Model`, un endpoint REST y mapeo de rutas. El resultado corresponde a la actividad “Información Personal” del laboratorio.
