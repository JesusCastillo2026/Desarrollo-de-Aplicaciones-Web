# Evaluación 3 — Gestión de pedidos de restaurante

**Asignación individual N.º 4:** Pedidos de restaurante. Microservicios asignados: Cliente, Plato y Pedido. Estudiante: CASTILLO SUMIRE, JESUS JOSE.

La implementación corresponde a la asignación individual N.º 4 del PDF de evaluación: Pedidos de restaurante, con los microservicios Cliente, Plato y Pedido. La colección se ejecutó en Postman Desktop y sus 28 aserciones aprobaron (0 fallidas, 0 errores). MySQL de XAMPP confirmó las tres bases y sus tablas. El informe PDF reúne capturas reales de ejecución, Postman, XAMPP, phpMyAdmin y VS Code.

## Objetivo

Separar la gestión de clientes, platos y pedidos en tres microservicios Spring Boot. Cada servicio expone su propia API REST y utiliza su propia base de datos MySQL. El servicio de pedidos consulta Cliente y Plato por REST para validar referencias y obtener el precio vigente de cada plato.

## Arquitectura y puertos

| Servicio | Puerto | Base de datos |
|---|---:|---|
| Cliente | 8081 | `bd_cliente` |
| Plato | 8082 | `bd_plato` |
| Pedido | 8083 | `bd_pedido` |

Los identificadores de cliente y plato se guardan en el pedido como referencias remotas. No se crean claves foráneas entre las bases de datos de distintos servicios.

## Requisitos

- Java 17 o superior.
- MySQL de XAMPP activo en el puerto 3306.
- Maven Wrapper incluido en esta carpeta.

## Ejecución local

Desde una terminal ubicada en esta carpeta, abre tres terminales y ejecuta una orden por terminal:

```powershell
.\mvnw.cmd -pl cliente-service spring-boot:run
.\mvnw.cmd -pl plato-service spring-boot:run
.\mvnw.cmd -pl pedido-service spring-boot:run
```

El perfil predeterminado es `dev`. En el primer arranque cada servicio crea su base si el usuario MySQL tiene permiso `CREATE DATABASE`; Hibernate crea o actualiza sus tablas con `ddl-auto: update`.

Para cambiar el perfil, establece `SPRING_PROFILES_ACTIVE=prod` y proporciona `SERVER_PORT`, `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`. En producción, Hibernate valida el esquema existente y no modifica su estructura. Pedido requiere además `CLIENTE_SERVICE_URL` y `PLATO_SERVICE_URL`.

## Endpoints implementados

### Cliente — `http://localhost:8081`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Consultar cliente |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

### Plato — `http://localhost:8082`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/platos` | Listar platos |
| GET | `/api/platos/{id}` | Consultar plato |
| POST | `/api/platos` | Crear plato |
| PUT | `/api/platos/{id}` | Actualizar plato |
| DELETE | `/api/platos/{id}` | Eliminar plato |

### Pedido — `http://localhost:8083`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/pedidos` | Listar pedidos con detalles |
| GET | `/api/pedidos/{id}` | Consultar pedido |
| POST | `/api/pedidos` | Crear pedido y calcular subtotales/total |
| PUT | `/api/pedidos/{id}` | Actualizar pedido y recalcular importes |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido |

Al crear un pedido, Pedido verifica por REST que exista el cliente y cada plato. El precio se obtiene desde Plato; el subtotal es precio por cantidad y el total suma los subtotales.

## Validaciones y errores

Los DTO de entrada validan campos requeridos, formatos de correo, cantidades y precios positivos. Los servicios informan recursos inexistentes, identificadores duplicados y errores al comunicarse con un microservicio; las respuestas incluyen estado HTTP, mensaje y ruta.

## Estado del avance y pendientes

- [x] Tres microservicios Maven, con puertos y bases separados.
- [x] Paquetes `model`, `dto`, `repository`, `services`, `controller` y `exception`.
- [x] Entidades JPA, repositorios, persistencia, relación pedido-detalle y cálculo de importes.
- [x] APIs REST, DTO, validaciones y comprobaciones de referencias remotas.
- [x] `application.yml` y perfiles `dev`/`prod`.
- [x] Colección ejecutada en Postman Desktop: 28 aserciones aprobadas, 0 fallidas y 0 errores.
- [x] Verificación de bases, tablas y registros demo en MariaDB de XAMPP.
- [x] Colección exportada en `postman/Evaluacion03_GestionPedidos.postman_collection.json`.
- [x] Capturas auténticas de Postman, XAMPP, phpMyAdmin, VS Code y arranque separado de Cliente, Plato y Pedido integradas en el PDF.
- [x] Informe PDF con objetivo, arquitectura, diagrama de servicios y bases, flujo, persistencia, código explicado, pruebas, cinco aprendizajes técnicos y una dificultad con su solución.
- [ ] Sustentación individual de 5 a 8 minutos, con demostración y respuesta a cambios pequeños en vivo.
- [x] Dominio y microservicios confirmados con el PDF individual: Pedidos de restaurante; Cliente, Plato y Pedido.
- [x] Referencias entre servicios representadas con IDs según la guía individual.

## Asignación confirmada

El PDF `Evaluacion 03 - D` asigna a CASTILLO SUMIRE, JESUS JOSE el laboratorio individual N.º 4: Pedidos de restaurante, con los microservicios Cliente, Plato y Pedido. Este proyecto corresponde a esa fila. Las referencias remotas se representan con IDs; cada servicio conserva su propia base MySQL.

## Entregables

- Código fuente y configuración de los tres microservicios en esta carpeta.
- Evidencia visual de las bases y tablas de MySQL en `evidencias/capturas/`.
- Colección y capturas del Runner de Postman en `postman/` y `evidencias/capturas/`.
- Informe final: `evidencias/EVALUACION_03_Gestion_Pedidos_Restaurante.pdf`.
- Guion de sustentación de 5 a 8 minutos incluido en el informe. La exposición en vivo queda pendiente de realizarse.
