# Semana 03 - Laboratorio 03: Construcción de Servicios Web RESTful

**Estado:** Implementación desarrollada y compilada. La ejecución de MySQL y las solicitudes de Postman quedan pendientes porque XAMPP no pudo iniciar en el entorno usado para preparar esta entrega. El código está en [modulo-ventas-api](modulo-ventas-api/); el informe con la implementación, explicación de código y dos conclusiones está en [Informe del Laboratorio 03](../Documentos/Laboratorios/Semana%203/Informe_Laboratorio_03.pdf).

## Objetivo

Desarrollar servicios RESTful con Spring Boot y arquitectura por capas para administrar productos y un módulo de ventas con categorías, productos, clientes, empleados, ventas y detalles, manteniendo sus relaciones en MySQL.

## Cómo se logró

Se implementaron operaciones CRUD, persistencia JPA/Hibernate, validaciones y manejo centralizado de errores. La venta relaciona cliente y empleado con productos mediante detalles; el servicio calcula totales y descuenta o repone stock en transacciones. Se incluye una colección de Postman para recorrer las operaciones del recurso de productos.

## Herramientas

Java 17+, Spring Boot, Gradle, Spring Web MVC, Spring Data JPA, Hibernate, MySQL de XAMPP, Visual Studio Code y Postman.

## Ejecución

Iniciar MySQL en XAMPP, abrir `modulo-ventas-api` en Visual Studio Code y ejecutar `.\gradlew.bat bootRun`. La aplicación queda disponible en `http://localhost:8080`; la configuración crea `laboratorio_api` si el usuario MySQL tiene permisos.

Para la secuencia de pruebas, importar [la colección Postman](modulo-ventas-api/postman/Semana03-Modulo-Ventas.postman_collection.json). El PDF documenta el código principal, las responsabilidades por capa y dos conclusiones.
