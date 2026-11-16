# Medical Appointments

API REST para la gestión de turnos de un centro médico con varias sedes.

## Funcionalidades
- [x] Esquema de base de datos con validaciones de solapamiento
- [x] Especialidades (CRUD)
- [x] Sedes (CRUD)
- [ ] Médicos
- [ ] Pacientes
- [ ] Disponibilidad de turnos
- [ ] Reserva y cancelación
- [ ] Autenticación con JWT y roles

## Tecnologías
Java 25 · Spring Boot · PostgreSQL · Flyway · Docker

## Requisitos
- Java 25
- Docker (tiene que estar abierto: la app levanta PostgreSQL automáticamente)

## Cómo correrlo
```bash
git clone https://github.com/juanmanuelgiulietti/medical-appointments.git
cd medical-appointments
./mvnw spring-boot:run
```
La API queda disponible en `http://localhost:8080`.

## Diseño

### DER
![Diagrama entidad-relación](docs/DER.png)

[Ver versión interactiva en dbdiagram](https://dbdiagram.io/d/DER-6a8dbe296440800f52de980c)

### Decisiones de negocio
- **No puede haber dos especialidades con el mismo nombre.** Se valida en el servicio al crear y al actualizar (responde 409) y la base lo garantiza con una restricción `UNIQUE`.
- **No puede haber dos sedes con el mismo nombre.** Se valida en el servicio al crear y al actualizar (responde 409) y la base lo garantiza con la restricción `uq_name_location`, que protege también de peticiones simultáneas.
- Los nombres y direcciones se guardan sin espacios al inicio ni al final.

## Endpoints

### Especialidades — `/api/specialties`
| Método | URL | Descripción | Respuestas |
|---|---|---|---|
| POST | `/api/specialties` | Crea una especialidad | 201, 400, 409 |
| GET | `/api/specialties` | Lista todas | 200 |
| GET | `/api/specialties/{id}` | Busca por id | 200, 404 |
| PUT | `/api/specialties/{id}` | Actualiza el nombre | 200, 400, 404, 409 |
| DELETE | `/api/specialties/{id}` | Elimina una especialidad | 204, 404 |

### Sedes — `/api/locations`
| Método | URL | Descripción | Respuestas |
|---|---|---|---|
| POST | `/api/locations` | Crea una sede | 201, 400, 409 |
| GET | `/api/locations` | Lista todas | 200 |
| GET | `/api/locations/{id}` | Busca por id | 200, 404 |
| PUT | `/api/locations/{id}` | Actualiza nombre y dirección | 200, 400, 404, 409 |
| DELETE | `/api/locations/{id}` | Elimina una sede | 204, 404 |

Los errores se devuelven en formato `ProblemDetail` (RFC 9457).