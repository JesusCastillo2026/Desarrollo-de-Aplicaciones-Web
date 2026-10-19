# Laboratorio 08 — Implementación de Tests

**Curso:** Desarrollo de Aplicaciones Web  
**Estudiante:** Jesus Castillo Sumire  
**Tema:** Pruebas unitarias e integración para una API REST con Spring Boot

## Objetivo

Comprobar el comportamiento de la API de productos mediante pruebas automatizadas y ampliar el proyecto con el mantenimiento completo de categorías. Las pruebas verifican los resultados correctos y también casos de error, datos inválidos y recursos inexistentes.

## Cómo se logró

Se partió del proyecto de productos desarrollado en la Semana 05 y se creó una copia independiente para este laboratorio. Se incorporaron pruebas unitarias de los servicios de productos y categorías, además de pruebas de integración HTTP para ambos controladores. El CRUD de categorías aplica validaciones, evita nombres duplicados y responde con códigos HTTP adecuados. Las pruebas usan una base H2 en memoria para no modificar los datos de MySQL.

## Requerimientos implementados

1. Pruebas unitarias para operaciones del servicio de productos, incluyendo la actualización y la consulta de un identificador inexistente.
2. Pruebas de integración del controlador de productos para creación, listado, actualización, eliminación y respuestas de error.
3. CRUD de categorías con entidad JPA, repositorio, servicio, DTO y controlador REST.
4. Validaciones de nombre y descripción, control de duplicados y respuestas globales para errores 400, 404 y 409.
5. Pruebas unitarias y de integración para categorías, aisladas de MySQL mediante H2.

## Herramientas

Java 17, Spring Boot 3.5.6, Spring Web, Spring Data JPA, Bean Validation, Maven, JUnit 5, Spring MockMvc, H2 para pruebas, MySQL/MariaDB de XAMPP para ejecución local, Visual Studio Code y Postman.

## Estructura

```text
src/main/java/com/tecsup/
  controller/   Endpoints de productos y categorías
  dto/          Datos de entrada y validaciones
  exception/    Errores de recurso y manejo global
  model/        Entidades JPA
  repository/   Persistencia con Spring Data JPA
  service/      Reglas de negocio
src/test/java/com/tecsup/
  controller/   Pruebas HTTP con MockMvc y H2
  service/      Pruebas unitarias de la lógica de negocio
src/test/resources/application.properties  Base temporal H2
database/crear_db.sql                      Creación de la BD para XAMPP
```

## Base de datos y ejecución

1. Iniciar **MySQL** en XAMPP. Apache no es necesario para esta API Spring Boot.
2. La base que usa la aplicación es `laboratorio_productos`. Se crea al iniciar Spring Boot si el usuario de MySQL tiene permiso, mediante `createDatabaseIfNotExist=true`. El archivo `database/crear_db.sql` permite crearla manualmente desde phpMyAdmin si se prefiere.
3. La aplicación crea o actualiza sus tablas JPA al iniciar. La configuración por defecto usa `root` sin contraseña; para otro entorno definir `DB_USERNAME` y `DB_PASSWORD`.
4. En la carpeta del proyecto ejecutar:

```powershell
./mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`. Para ejecutar todas las pruebas:

```powershell
./mvnw.cmd clean test
```

Las pruebas usan H2 en memoria y no requieren que XAMPP esté encendido.

## Flujo

```text
Postman o MockMvc → Controller → DTO / Validación → Service → Repository → MySQL (ejecución)
                                                                       ↘ H2 (pruebas)
```

## Endpoints principales

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/productos` | Lista productos |
| GET | `/api/productos/{id}` | Consulta un producto; 404 si no existe |
| POST | `/api/productos` | Crea un producto; 201 si es válido |
| PUT | `/api/productos/{id}` | Actualiza un producto; 404 si no existe |
| DELETE | `/api/productos/{id}` | Elimina un producto |
| GET | `/api/categorias` | Lista categorías |
| GET | `/api/categorias/{id}` | Consulta una categoría |
| POST | `/api/categorias` | Crea una categoría; entrega `Location` y 201 |
| PUT | `/api/categorias/{id}` | Actualiza una categoría |
| DELETE | `/api/categorias/{id}` | Elimina una categoría; 204 |

Ejemplo para crear categoría:

```http
POST http://localhost:8080/api/categorias
Content-Type: application/json
```

```json
{
  "nombre": "Accesorios",
  "descripcion": "Periféricos y complementos"
}
```

## Pruebas realizadas

Ejecución verificada con `./mvnw.cmd clean test`: **28 pruebas**, 0 fallidas, 0 errores y 0 omitidas.

| Clase de prueba | Cobertura | Resultado |
|---|---|---|
| `ProductoServiceTest` | Reglas del servicio de productos | 7 aprobadas |
| `ProductoControllerTest` | Respuestas HTTP y CRUD de productos | 7 aprobadas |
| `CategoriaServiceTest` | Reglas CRUD, inexistentes y duplicados | 7 aprobadas |
| `CategoriaControllerTest` | Respuestas HTTP, validación y CRUD de categorías | 7 aprobadas |

El informe PDF contiene el desarrollo, fragmentos de código en cuadros sin relleno, flujo de datos, evidencias de la ejecución y conclusiones.

## Observaciones

1. Las pruebas unitarias permiten revisar la lógica de cada servicio sin iniciar una conexión con MySQL ni depender de los datos de XAMPP.
2. MockMvc prueba las rutas HTTP y las respuestas reales de los controladores dentro del contexto de Spring.
3. La base H2 se crea únicamente para las pruebas y se destruye al terminar, por lo que los datos de `laboratorio_productos` permanecen separados.
4. Las categorías ahora cuentan con su propia entidad y operaciones CRUD. El campo categoría que ya tenía producto se conserva como texto para mantener la compatibilidad con el proyecto de Semana 05.
5. La verificación completa terminó con 28 pruebas aprobadas, incluyendo entradas inválidas, registros repetidos y búsquedas que no encuentran el recurso.

## Conclusiones

1. Implementar pruebas unitarias ayudó a comprobar los resultados de los servicios con distintos escenarios antes de depender de una ejecución manual.
2. Las pruebas de integración confirmaron que las rutas conectan el controlador, la validación, el servicio y la persistencia como se espera.
3. El CRUD de categorías amplía la API y muestra cómo organizar una nueva funcionalidad en capas sin mezclar las responsabilidades.
4. Usar H2 como base temporal facilita repetir las pruebas y evita modificar registros reales o requerir que MySQL esté disponible.
5. La ejecución completa de Maven terminó sin fallos; por eso el proyecto queda con una verificación automatizada que se puede repetir mediante `./mvnw.cmd clean test`.
