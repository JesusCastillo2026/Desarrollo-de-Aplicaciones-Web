# Evaluación 3 — Gestión de pedidos de restaurante

Avance implementado hasta el punto 5 de la lista: servicios, paquetes, entidades y persistencia, API REST/reglas y configuración YAML con perfiles `dev` y `prod`. Las pruebas Postman y las evidencias quedan para completar después.

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
- [ ] Pruebas manuales en Postman y casos válidos e inválidos.
- [ ] Capturas/colección Postman, evidencia SQL, conclusiones y sustentación.
