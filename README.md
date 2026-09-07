# Pedidos360 - Aplicación Frontend Angular

Interfaz web cliente para la plataforma Pedidos360, encargada del despliegue del catálogo de productos y la interacción dinámica con el carrito de compras del usuario.

## 🛠️ Tecnologías Utilizadas

- Angular 17+
- TypeScript
- HTML5 / CSS3
- RxJS
- Angular Router

## 🔐 Integración con Backend

- Conexión con ms-carrito-compras en http://localhost:8082.
- Transmisión de cabeceras de autorización (Authorization: Bearer <JWT>) para endpoints protegidos.
- Control de estados de interfaz (gestión visual y bloqueo de acciones para carritos en estado CONFIRMADO).

## 🚀 Requisitos e Instalación

### Requisitos previos
- Node.js (v18 o superior)
- npm (v9 o superior)
- Angular CLI (npm install -g @angular/cli)

### Ejecución local

1. Clonar el repositorio:
   git clone <URL_DEL_REPOSITORIO_FRONTEND>
   cd pedidos360-frontend

2. Instalar dependencias:
   npm install

3. Levantar el servidor de desarrollo:
   ng serve

   *Acceder en el navegador a http://localhost:4200.*

## 📦 Estructura del Proyecto

src/
├── app/
│   ├── components/
│   │   ├── carrito/        # Componente y vista del carrito de compras
│   │   └── productos/      # Componente del catálogo de productos
│   ├── services/           # Servicios HTTP para comunicación con microservicios
│   └── app.routes.ts      # Enrutamiento de la aplicación
└── assets/                 # Recursos estáticos (imágenes, estilos globales)
