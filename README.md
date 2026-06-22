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

## 🛠️ Instalación y Execución

Para arrancar el servidor localmente, ejecuta el siguiente comando en la raíz del proyecto:

```bash
./mvnw spring-boot:run
```
---

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
./mvnw clean test

---

## 📊 Casos de Prueba Validados



A continuación se presentan los casos de prueba organizados y validados de forma clara y estructurada.

| Identificador | Fecha Aplicación | Producto | Cadena (Brand) | Resultado Esperado | Tarifa Aplicada (Price List) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Test 1** | 2020-06-14 10:00:00 | 354551 | 1 (ZARA) | 35.50 EUR | Price List 1 |
| **Test 2** | 2020-06-14 16:00:00 | 354551 | 1 (ZARA) | 25.45 EUR | Price List 2 |
| **Test 3** | 2020-06-14 21:00:00 | 354551 | 1 (ZARA) | 35.50 EUR | Price List 1 |
| **Test 4** | 2020-06-15 10:00:00 | 354551 | 1 (ZARA) | 30.50 EUR | Price List 3 |
| **Test 5** | 2020-06-16 21:00:00 | 354551 | 1 (ZARA) | 38.95 EUR | Price List 4 |
| **Extra 1** | 2020-06-14 10:00:00 | 99999 (Inexistente) | 1 (ZARA) | 404 Not Found | Ninguna |
| **Extra 2** | fecha-incorrecta | 354551 | 1 (ZARA) | 400 Bad Request | Ninguna |

---
*Nota: Se ha homogeneizado el campo de la cadena a "1 (ZARA)" basándose en las pruebas de error para mantener la coherencia semántica.*

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

---

## 📈 Próximas Mejoras (Production Ready Roadmap)
Pensando en la integración futura de este servicio dentro de un ecosistema empresarial complejo (conectado de forma nativa con Checkouts, Stock o Frontends externos), se plantean las siguientes evoluciones:


### Capa de Caché Distribuida (Redis): 
Añadir una capa de caché con desalojo por TTL para las consultas de tarifas recurrentes, optimizando el rendimiento y evitando la sobrecarga de lecturas en la base de datos relacional.
###  Migración de la comunicación interna a gRPC (HTTP/2)
Actualmente, la aplicación está aislada, pero en un ecosistema complejo, la comunicación entre microservicios mediante REST/JSON genera sobrecarga (overhead) debido a la serialización y el tamaño de los payloads.
* **Propuesta:** Implementar **gRPC** para la intercomunicación entre este servicio y futuros módulos.
* **Beneficio para el negocio:** Reducción drástica de la latencia en redes internas, menor consumo de ancho de banda gracias a la serialización binaria con Protocol Buffers (`.proto`) y contratos de API fuertemente tipados que evitan errores en despliegues independientes.

### Adopción de un enfoque "API-First" con Protocol Buffers
* **Propuesta:** Definir los contratos de las entidades globales utilizando archivos `.proto` compartidos.
* **Beneficio:** Esto permitiría auto-generar el código del cliente y del servidor en cualquier lenguaje de programación (Java, Go, Node.js), facilitando que diferentes equipos de Inditex consuman o expongan servicios sin fricción y manteniendo una única fuente de verdad para los datos.



### 🛡️ Seguridad Completa
Autenticación y Autorización (OAuth2 / JWT): Asegurar el endpoint integrando Spring Security con un servidor de identidad (como Keycloak) mediante JSON Web Tokens para verificar roles y scopes.

Validación de Datos Estricta: Incorporar anotaciones de jakarta.validation (@Min, @NotNull) en los parámetros de entrada de los controladores para denegar de forma temprana peticiones malformadas.

### 📊 Resiliencia, Observabilidad y DevOps
Métricas y Trazabilidad Centralizada: Añadir Spring Boot Actuator, Micrometer y OpenTelemetry para exportar métricas de rendimiento a Prometheus/Grafana y trazas distribuidas a Zipkin/Jaeger.

Contenedorización Estándar: Crear un archivo Dockerfile multi-stage optimizado utilizando imágenes base distroless o Alpine para reducir la superficie de ataque del despliegue.

Estrategia de Rate Limiting: Implementar un filtro de control de flujo para proteger el servicio contra picos imprevistos de tráfico.

