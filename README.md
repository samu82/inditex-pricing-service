Markdown
# Inditex Pricing Service - Prueba Técnica

Servicio REST construido con **Spring Boot 3** y **Java 17** para la consulta de tarifas de precios de productos filtrado por cadena (brand), producto y fecha de aplicación, resolviendo conflictos mediante un sistema de prioridades.

---

## 🏗️ Arquitectura y Diseño

El proyecto ha sido diseñado siguiendo los principios de la **Arquitectura Hexagonal (Ports & Adapters)** y **Domain-Driven Design (DDD)** para garantizar un desacoplamiento absoluto del framework y la infraestructura:

* **`domain.model`**: Contiene las entidades del núcleo de negocio (`Price` utilizando Java Records inmutables) libres de dependencias o anotaciones de Spring/JPA.
* **`ports.inbound` / `ports.outbound`**: Interfaces puras que definen los contratos de entrada (Casos de Uso) y salida (Persistencia).
* **`infrastructure.inbound.rest`**: Adaptador de entrada que expone la API REST mediante controladores y maneja las excepciones globales de forma semántica (`404 Not Found`, `400 Bad Request`).
* **`infrastructure.outbound.db`**: Adaptador de salida que implementa la persistencia utilizando Spring Data JPA sobre una base de datos **H2 en memoria**.

---

## 🚀 Requisitos Previos

* Java 17 (JDK)
* Maven 3.x (o utilizar el Wrapper incluido `./mvnw`)

---

## 🛠️ Instalación y Ejecución

Para arrancar el servidor localmente, ejecuta el siguiente comando en la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

## 🗄️ Acceso a la Base de Datos (H2 Console)
La base de datos se inicializa automáticamente con los datos de prueba del enunciado (schema.sql y data.sql). Puedes acceder a la consola interactiva en:

URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:pricingdb

User: sa

Password: (En blanco)

---

## 🧪 Ejecución de Tests Automatizados
Se han automatizado los 5 escenarios de prueba obligatorios detallados en el enunciado, sumando pruebas adicionales de robustez mediante tests de integración/E2E utilizando MockMvc levantando el contexto real de la aplicación:

Bash
```
./mvnw clean test
```

## 📊 Casos de Prueba Validados
A continuación se presentan los casos de prueba organizados y validados de forma clara y estructurada.

| Identificador | Fecha Aplicación | Producto | Cadena (Brand) | Resultado Esperado | Tarifa Aplicada (Price List) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| Test 1 | 2020-06-14 10:00:00 | 354551 | 1 (ZARA) | 35.50 EUR | Price List 1 |
| Test 2 | 2020-06-14 16:00:00 | 354551 | 1 (ZARA) | 25.45 EUR | Price List 2 |
| Test 3 | 2020-06-14 21:00:00 | 354551 | 1 (ZARA) | 35.50 EUR | Price List 1 |
| Test 4 | 2020-06-15 10:00:00 | 354551 | 1 (ZARA) | 30.50 EUR | Price List 3 |
| Test 5 | 2020-06-16 21:00:00 | 354551 | 1 (ZARA) | 38.95 EUR | Price List 4 |
| Extra 1 | 2020-06-14 10:00:00 | 99999 (Inexistente) | 1 (ZARA) | 404 Not Found | Ninguna |
| Extra 2 | fecha-incorrecta | 354551 | 1 (ZARA) | 400 Bad Request | Ninguna |

---

## 🔌 Ejemplo de Consulta (API Endpoint)

Petición (GET):

HTTP
GET http://localhost:8080/api/v1/prices?brandId=1&productId=35455&applicationDate=2020-06-14T16:00:00
Respuesta Exitosa (200 OK):

JSON
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}

## 📦 Portabilidad (Ejecución en cualquier equipo)
Este proyecto está configurado para ser completamente portable, lo que significa que puedes descargarlo y ejecutarlo en cualquier sistema operativo (Windows, macOS o Linux) sin necesidad de tener Maven instalado en tu sistema.

Para lograr esto, el repositorio incluye Maven Wrapper. Este componente se encarga de descargar y configurar automáticamente la versión exacta de Maven requerida para el proyecto (3.9.16) la primera vez que se ejecuta.

Cómo ejecutar el proyecto en tu máquina:
Clona el repositorio en tu equipo.

Abre una terminal en la raíz del proyecto.

Ejecuta el comando correspondiente a tu sistema operativo:

En Windows (Command Prompt o PowerShell):

Bash
```
  mvnw.cmd clean install
```
En Linux / macOS:

Bash
```
  ./mvnw clean install
```
Gracias a los archivos de configuración incluidos (.gitignore, .gitattributes y .mvn/), el entorno se mantendrá limpio, los saltos de línea se adaptarán a tu sistema operativo y se evitará el clásico problema de "en mi máquina sí funciona".

## 📈 Próximas Mejoras (Production Ready Roadmap)
Pensando en la integración futura de este servicio dentro de un ecosistema empresarial complejo (conectado de forma nativa con Checkouts, Stock o Frontends externos), se plantean las siguientes evoluciones basadas en componentes Open Source de alto rendimiento:

### 🚀 Rendimiento y Comunicación Eficiente
Capa de Caché Distribuida e Invalidación Híbrida (Redis): Añadir una capa de caché para las consultas de tarifas recurrentes optimizando lecturas. Se combinará una estrategia de desalojo por TTL con un mecanismo de evicción basado en eventos (ej. escuchando actualizaciones de precios mediante Kafka o RabbitMQ) para garantizar consistencia eventual e inmediata frente a cambios repentinos de tarifas.

Migración de la Comunicación Interna a gRPC (HTTP/2): Sustituir el transporte REST/JSON interno para evitar el overhead de serialización en el ecosistema de microservicios.

Beneficio para el negocio: Reducción drástica de latencia en redes internas, menor consumo de ancho de banda y contratos fuertemente tipados a través de búferes de protocolo binarios (.proto) que evitan errores en despliegues independientes.

Enfoque "API-First" con Protocol Buffers: Definir los contratos de las entidades de precios globalmente compartidas utilizando archivos .proto. Esto permitirá auto-generar los clientes y servidores en múltiples lenguajes (Java, Go, Node.js), habilitando que diferentes equipos consuman o expongan servicios de forma agnóstica con una única fuente de verdad para los datos.

### 🛡️ Seguridad Completa y Validación de Contratos
Autenticación y Autorización Centralizada (OAuth2 / JWT): Asegurar los endpoints integrando Spring Security con un servidor de identidad Open Source (Keycloak), validando roles y scopes mediante JSON Web Tokens.

Validación de Datos en Arquitectura Híbrida:

Para los endpoints externos expuestos en REST, incorporación de anotaciones estrictas de jakarta.validation (@Min, @NotNull) en los parámetros de entrada de los controladores.

Para la intercomunicación interna mediante gRPC, adopción de protoc-gen-validate (PGV) para compilar reglas de validación directamente en los archivos .proto, garantizando el rechazo temprano de payloads malformados sin acoplamiento al framework de aplicación.

### 📊 Resiliencia, Observabilidad y DevOps
Métricas y Trazabilidad Centralizada: Integración de Spring Boot Actuator, Micrometer y OpenTelemetry para exportar de forma estandarizada métricas de rendimiento a Prometheus/Grafana y trazas distribuidas del ciclo de vida de los precios a Jaeger.

Contenedorización Estándar y Segura: Creación de un pipeline con Dockerfile multi-stage optimizado utilizando imágenes base Alpine o Distroless para reducir drásticamente el tamaño final del artefacto y mitigar la superficie de ataque en el despliegue.

Estrategia de Rate Limiting Distribuido: Implementar un control de flujo y protección contra picos imprevistos de tráfico (Throttling) reutilizando la infraestructura de Redis (ej. algoritmo Token Bucket), evitando cuellos de botella y asegurando la alta disponibilidad de la base de datos relacional.




