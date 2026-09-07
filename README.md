# ms-productos-catalogo

Microservicio de catálogo de productos del sistema **Pedidos360**. Expone
los productos de la tienda digital vía REST. En producción, este servicio
corre en una instancia EC2 y solo debe ser alcanzable a través del
**AWS API Gateway** (no expuesto directamente a internet).

## Estructura

```
src/main/java/cl/duoc/pedidos360/productos/
 ├─ ProductosCatalogoApplication.java   Clase de arranque
 ├─ config/SecurityConfig.java          Resource Server (valida JWT de Azure AD)
 ├─ controller/ProductoController.java  Endpoints REST (/api/productos)
 ├─ dto/                                Request/Response (no expone la entidad directo)
 ├─ entity/Producto.java                Entidad JPA
 ├─ repository/ProductoRepository.java  Acceso a datos (Spring Data JPA)
 ├─ service/                            Lógica de negocio
 └─ exception/                          Manejo de errores centralizado
```

## Cómo correrlo localmente

```bash
./mvnw spring-boot:run
# o, si no tienes el wrapper generado aún:
mvn spring-boot:run
```

Por defecto usa el perfil `dev` (`application-dev.yml`) con base de datos
H2 en memoria. La consola H2 queda disponible en `http://localhost:8081/h2-console`.

## Pendiente para completar el encargo (EP1)

- [ ] Ejecutar `mvn -N wrapper:wrapper` para generar `mvnw`/`mvnw.cmd` si tu
      repo los requiere (o usar tu Maven local).
- [ ] Reemplazar `{tenant-id}` en `application-dev.yml` una vez creado el
      tenant de Azure AD.
- [ ] Escribir pruebas adicionales (casos de error, actualización, borrado).
- [ ] Ajustar las reglas de `authorizeHttpRequests` en `SecurityConfig`
      según qué endpoints deban ser públicos vs. protegidos.

## Pendiente para el despliegue (EP2)

- [ ] Desplegar en una instancia EC2 (o contenedor) en AWS.
- [ ] Registrar esta URL como *backend* de una ruta en AWS API Gateway.
- [ ] Configurar el JWT Authorizer del API Gateway apuntando al JWKS de
      Azure AD (`https://login.microsoftonline.com/{tenant-id}/discovery/v2.0/keys`).
- [ ] Migrar `application-prod.yml` con el endpoint real de RDS.
