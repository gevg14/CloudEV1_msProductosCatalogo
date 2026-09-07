package cl.duoc.pedidos360.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio de Catalogo de Productos - Pedidos360.
 *
 * Expone los productos de la tienda digital. Sus rutas se integran y
 * exponen hacia el frontend a traves del AWS API Gateway, que actua
 * como unico punto de entrada (single entrypoint) del sistema.
 */
@SpringBootApplication
public class ProductosCatalogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductosCatalogoApplication.class, args);
    }
}
