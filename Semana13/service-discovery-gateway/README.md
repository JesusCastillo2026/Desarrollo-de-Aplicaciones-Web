# Laboratorio 13 - Service Discovery y API Gateway

## Objetivo
Integrar un servidor Eureka para registrar y descubrir microservicios, y centralizar el acceso REST con Spring Cloud Gateway. Se publican los servicios de categorias, productos, clientes y pedidos en rutas estables del Gateway.

## Arquitectura y responsabilidades

| Componente | Puerto | Responsabilidad |
|---|---:|---|
| Eureka Server | 8761 | Registro y descubrimiento de instancias |
| Gateway | 8080 | Entrada HTTP, rutas y balanceo por nombre de servicio |
| Categoria Service | 8082 | API y persistencia de categorias |
| Producto Service | 8083 | API y persistencia de productos; consulta categorias por HTTP |
| Cliente Service | 8081 | API y persistencia de clientes |
| Pedido Service | 8084 | API y persistencia de pedidos; consulta clientes y productos por HTTP |

Las cuatro APIs de negocio mantienen bases separadas en MySQL. Las referencias entre servicios viajan como identificadores y se resuelven mediante llamadas REST; no se crean claves foraneas entre bases distintas.

## Requisitos
- Java 17
- MySQL de XAMPP en `localhost:3306`
- Maven Wrapper incluido (`mvnw.cmd`)
- Puerto local libre: 8080, 8081, 8082, 8083, 8084 y 8761

## Bases de datos de desarrollo

| Servicio | Base predeterminada |
|---|---|
| categoria-service | `bd_categoria_dev` |
| producto-service | `bd_producto_dev` |
| cliente-service | `bd_cliente_dev` |
| pedido-service | `bd_pedido_dev` |

En el perfil `dev`, `createDatabaseIfNotExist=true` permite crear cada esquema y Hibernate actualiza las tablas con `ddl-auto: update`. Credenciales predeterminadas para XAMPP: usuario `root`, contrasena vacia. Ajusta las variables `DB_URL`, `DB_USER` y `DB_PASSWORD` si tu instalacion usa otros valores. No uses esa configuracion para produccion.

## Ejecucion desde PowerShell

Abre seis terminales en esta carpeta y ejecuta un comando por terminal. Mantén cada proceso activo.

1. Eureka:
   `.\mvnw.cmd -pl microservicios\eureka-server spring-boot:run`
2. Categoria:
   `.\mvnw.cmd -pl microservicios\categoria-service spring-boot:run`
3. Producto:
   `.\mvnw.cmd -pl microservicios\producto-service spring-boot:run`
4. Cliente:
   `.\mvnw.cmd -pl microservicios\cliente-service spring-boot:run`
5. Pedido:
   `.\mvnw.cmd -pl microservicios\pedido-service spring-boot:run`
6. Gateway:
   `.\mvnw.cmd -pl microservicios\gateway-service spring-boot:run`

Abre `http://localhost:8761` para revisar el registro de Eureka y usa el Gateway en `http://localhost:8080`.

## Rutas publicadas

| Metodo | URL | Servicio destino |
|---|---|---|
| GET | `/api/categorias` | categoria-service |
| GET | `/api/productos` | producto-service |
| GET | `/api/clientes` | cliente-service |
| GET | `/api/pedidos` | pedido-service |
| GET | `/actuator/health` | Estado del Gateway |

Cada API tambien ofrece sus operaciones CRUD en su ruta `/api/...`.

## Verificacion realizada
- Los cinco servicios de negocio e infraestructura aparecieron registrados como `UP` en Eureka.
- Las cuatro rutas CRUD de consulta respondieron HTTP 200 a traves del Gateway.
- `/actuator/health` del Gateway respondio `{"status":"UP"}`.
- La compilacion Maven finalizo con `BUILD SUCCESS`. Se omitieron las pruebas automatizadas durante esa compilacion; las rutas anteriores se comprobaron con solicitudes HTTP reales.
- Evidencia visual guardada en `evidencias/01-Eureka-Registro.pdf`.

Durante el arranque local Eureka puede mostrar temporalmente su aviso de renovaciones/autoproteccion; comprueba que las aplicaciones esten `UP` y valida las rutas del Gateway.

