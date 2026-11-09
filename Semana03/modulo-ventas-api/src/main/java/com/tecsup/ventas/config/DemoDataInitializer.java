package com.tecsup.ventas.config;

import com.tecsup.ventas.model.*;
import com.tecsup.ventas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Component
public class DemoDataInitializer implements CommandLineRunner {
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    private final EmpleadoRepository empleados;
    private final VentaRepository ventas;

    public DemoDataInitializer(CategoriaRepository categorias, ProductoRepository productos,
                               ClienteRepository clientes, EmpleadoRepository empleados, VentaRepository ventas) {
        this.categorias=categorias; this.productos=productos; this.clientes=clientes;
        this.empleados=empleados; this.ventas=ventas;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (categorias.count()+productos.count()+clientes.count()+empleados.count()+ventas.count()>0) return;

        Categoria tecnologia=categorias.save(new Categoria("Tecnología","Equipos electrónicos y computación"));
        Categoria accesorios=categorias.save(new Categoria("Accesorios","Complementos para equipos"));
        Producto laptop=productos.save(new Producto("Laptop HP Victus 15",new BigDecimal("3500.50"),12,tecnologia));
        productos.save(new Producto("Xiaomi 15",new BigDecimal("3150.00"),20,tecnologia));
        productos.save(new Producto("Audífonos Redmi",new BigDecimal("180.00"),35,accesorios));
        Cliente cliente=clientes.save(new Cliente("Ana","Torres","ana.torres@example.com","74851236","987654321"));
        Empleado empleado=empleados.save(new Empleado("Luis","Paredes","luis.paredes@example.com","Vendedor"));

        Venta venta=new Venta(cliente,empleado,EstadoVenta.REGISTRADA);
        laptop.setStock(laptop.getStock()-2);
        venta.getDetalles().add(new DetalleVenta(venta,laptop,2,laptop.getPrecio()));
        ventas.save(venta);
    }
}
