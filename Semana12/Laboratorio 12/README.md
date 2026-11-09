# Laboratorio 12 - Comunicación entre servicios

**Curso:** Desarrollo de Aplicaciones Web  
**Sección:** C24-D  
**Estudiante:** Jesús Castillo Sumire

## Objetivo

Registrar los microservicios de una tienda en Eureka y comunicarlos mediante OpenFeign. `producto-service` consulta las categorías y `pedido-service` obtiene los datos del cliente y del producto al crear o consultar un pedido. Cada servicio conserva su propia base de datos y solo guarda identificadores de referencias externas.

## Estructura

```text
microservicios/
├── eureka-server/      # Registro y descubrimiento, puerto 8761
├── categoria-service/  # API de categorías, puerto 8082
├── cliente-service/    # API de clientes, puerto 8081
├── producto-service/   # API de productos + Feign a categoría, puerto 8083
├── pedido-service/     # API de pedidos + Feign a cliente y producto, puerto 8084
├── database/           # Script para crear las bases independientes
└── postman/            # Colección de flujos positivos y de validación
```

Los microservicios usan Spring Boot 4.1.0, Java 21 y Spring Cloud 2025.1.2. El tren Spring Cloud 2025.1 es compatible con Spring Boot 4.1.x. Se mantiene la configuración de MariaDB/MySQL usada por XAMPP.

## Bases de datos

El script `microservicios/database/create-databases.sql` crea `bd_categoria_dev`, `bd_cliente_dev`, `bd_producto_dev` y `bd_pedido_dev`. Importarlo en phpMyAdmin con MySQL de XAMPP activo en el puerto 3306. Hibernate crea o actualiza las tablas del ambiente `dev` al iniciar cada servicio. No se crean relaciones JPA entre microservicios.

## Ejecución en Windows

Abrir una terminal de VS Code en `Semana12/Laboratorio 12/microservicios`. Iniciar XAMPP MySQL y luego abrir cinco terminales independientes. Ejecutar un servicio por terminal, en este orden:

```powershell
.\mvnw.cmd -pl eureka-server spring-boot:run
.\mvnw.cmd -pl categoria-service spring-boot:run
.\mvnw.cmd -pl cliente-service spring-boot:run
.\mvnw.cmd -pl producto-service spring-boot:run
.\mvnw.cmd -pl pedido-service spring-boot:run
```

El panel de Eureka queda en `http://localhost:8761`. Esperar a que se registren `CATEGORIA-SERVICE`, `CLIENTE-SERVICE`, `PRODUCTO-SERVICE` y `PEDIDO-SERVICE` antes de probar las llamadas Feign. Las bases deben existir y el usuario configurado en los `application-dev.yml` debe tener permisos.

Para verificar compilación sin iniciar servicios:

```powershell
.\mvnw.cmd -pl eureka-server,categoria-service,cliente-service,producto-service,pedido-service -DskipTests compile
```

## Flujo principal

1. Crear una categoría y un cliente.
2. Crear un producto con el identificador de la categoría. `producto-service` valida la categoría a través de Feign y Eureka.
3. Crear un pedido con `idCliente`, `idProducto` y `cantidad`. `pedido-service` valida ambos IDs, calcula el total con el precio recibido de `producto-service` y persiste únicamente los IDs, cantidades, fecha, estado y total.
4. Consultar `GET /api/pedidos/{id}`. La respuesta agrega cliente y productos en tiempo de consulta, sin leer otras bases de datos.
5. Cambiar el estado del pedido con `PATCH /api/pedidos/{id}/estado`; los valores permitidos son `PENDIENTE`, `ANULADO` y `ATENDIDO`. Los pedidos no se borran.

## Endpoints

| Servicio | Puerto | Rutas principales |
|---|---:|---|
| Categoría | 8082 | `GET/POST /api/categorias`, `GET/PUT/DELETE /api/categorias/{id}` |
| Cliente | 8081 | `GET/POST /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}` |
| Producto | 8083 | `GET/POST /api/productos`, `GET/PUT/DELETE /api/productos/{id}` |
| Pedido | 8084 | `GET/POST /api/pedidos`, `GET/PUT /api/pedidos/{id}`, `PATCH /api/pedidos/{id}/estado` |

## Ejemplo del pedido

`POST http://localhost:8084/api/pedidos`

```json
{
  "fecha": "2026-11-09",
  "idCliente": 1,
  "detalles": [
    { "idProducto": 1, "cantidad": 2 }
  ]
}
```

La respuesta contiene datos del cliente y el producto recuperados por Feign, además del subtotal global calculado. El request de Postman guarda los IDs que devuelve el flujo para que la colección pueda ejecutarse en secuencia.

## Postman

Importar `microservicios/postman/Laboratorio-12.postman_collection.json` y ejecutar en orden. Incluye registro de Eureka, categoría, cliente y producto; creación y lectura del pedido con respuesta agregada; cambio de estado; y validaciones negativas para cliente, producto y cantidad.

## Evidencia y alcance de verificación

La colección queda lista para ejecutar cuando MySQL de XAMPP esté activo. En el equipo donde se preparó el proyecto, el puerto 3306 no estaba disponible; por eso la documentación no presenta capturas ni resultados Postman como si se hubieran ejecutado. El informe PDF identifica esa limitación y muestra el diagrama del flujo.

