# Medical Appointments

API REST para la gestión de turnos de un centro médico con varias sedes.

## Funcionalidades
- [x] Esquema de base de datos con validaciones de solapamiento
- [x] Especialidades (CRUD)
- [x] Sedes (CRUD)
- [x] Tipos de consulta (CRUD)
- [x] Usuarios con roles y contraseña inicial (base para médicos y pacientes)
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

### Credenciales del administrador (solo desarrollo)
Al levantar la app se crea un administrador para probar en local:
- Email: `admin@example.com`
- Contraseña: `test1234`

Son credenciales de desarrollo: no deben usarse en producción.

## Diseño

### DER
![Diagrama entidad-relación](docs/DER.png)

[Ver versión interactiva en dbdiagram](https://dbdiagram.io/d/DER-6a8dbe296440800f52de980c)

### Decisiones de negocio

#### Generales
- Los nombres y direcciones se guardan sin espacios al inicio ni al final.

#### Especialidades
- **No puede haber dos especialidades con el mismo nombre.** Se valida en el servicio al crear y al actualizar (responde 409) y la base lo garantiza con una restricción `UNIQUE`.

#### Sedes
- **No puede haber dos sedes con el mismo nombre.** Se valida en el servicio al crear y al actualizar (responde 409) y la base lo garantiza con la restricción `uq_name_location`, que protege también de peticiones simultáneas.

#### Tipos de consulta
- **La especialidad de un tipo de consulta no se puede cambiar**, para no alterar el historial de turnos ya dados.
- **El nombre de un tipo de consulta es único dentro de cada especialidad**, no en general: "Control" puede existir en Cardiología y en Dermatología, pero no dos veces en la misma. Se valida en el servicio (responde 409) y la base lo garantiza con `UNIQUE (specialty_id, name)`.
- **La duración tiene que ser mayor que cero.** Se valida en el DTO (responde 400) y la base lo garantiza con un `CHECK`.
- **Un tipo de consulta solo se accede a través de su especialidad**: pedirlo con una especialidad que no es la suya responde 404, aunque el id exista.
- Listar los tipos de consulta de una especialidad que no existe responde 404; si la especialidad existe pero no tiene tipos de consulta, devuelve una lista vacía.

#### Usuarios

##### Creación
- **Los usuarios no tienen endpoints propios.** Se crean solo a través del alta de médico (la hace un admin) y del registro de paciente (lo hace un recepcionista), en la misma operación que el médico o el paciente.
- **El usuario se crea con un solo rol:** `DOCTOR` en el alta de médico, `PATIENT` en el de paciente.
- **El email es único y se guarda en minúsculas y sin espacios.** Se valida en el servicio (responde 409) y la base lo garantiza con `UNIQUE`.
- El teléfono es opcional.

##### Roles
- **Un usuario puede tener varios roles.** Los roles posibles son `PATIENT`, `DOCTOR`, `RECEPTIONIST` y `ADMIN`.

##### Contraseñas
- **La contraseña inicial se genera automáticamente:** 12 caracteres al azar, sin los que se confunden al dictarla (`0`, `O`, `1`, `l`, `I`).
- **Solo se guarda su hash (BCrypt).** La contraseña en texto plano se muestra una única vez, en la respuesta del alta, y no se puede recuperar.

##### Estado
- **Los usuarios no se borran: se desactivan.** Todo usuario nace activo y la fecha de creación la asigna la base.

##### Administrador inicial
- El primer administrador se crea con una migración (V3). Ver las [credenciales de desarrollo](#credenciales-del-administrador-solo-desarrollo).

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

### Tipos de consulta — `/api/specialties/{specialtyId}/consultation-types`
| Método | URL | Descripción | Respuestas |
|---|---|---|---|
| POST | `/api/specialties/{specialtyId}/consultation-types` | Crea un tipo de consulta en la especialidad | 201, 400, 404, 409 |
| GET | `/api/specialties/{specialtyId}/consultation-types` | Lista los tipos de consulta de la especialidad | 200, 404 |
| GET | `/api/specialties/{specialtyId}/consultation-types/{id}` | Busca un tipo de consulta de la especialidad | 200, 404 |
| PUT | `/api/specialties/{specialtyId}/consultation-types/{id}` | Actualiza nombre y duración | 200, 400, 404, 409 |
| DELETE | `/api/specialties/{specialtyId}/consultation-types/{id}` | Elimina un tipo de consulta | 204, 404 |

Los errores se devuelven en formato `ProblemDetail` (RFC 9457).

## Pendientes conocidos
- Eliminar una especialidad o un tipo de consulta que esté en uso (por ejemplo, con tipos de consulta, médicos o turnos asociados) hoy responde 500, porque la base rechaza el borrado por la clave foránea. Se evaluará responder 409 o usar baja lógica (soft delete) para conservar el historial.
- Desactivar existe en la entidad (`deactivate`), pero todavía no hay un endpoint que lo use.
- Un usuario inactivo no debería poder iniciar sesión: se aplica cuando haya login.
- Agregar un rol a un usuario existente (el médico que además es admin): falta la operación.
- Obligar a cambiar la contraseña inicial en el primer ingreso: requiere una columna nueva.
- El administrador de desarrollo no puede existir así en producción: ahí se crearía con variables de entorno.
