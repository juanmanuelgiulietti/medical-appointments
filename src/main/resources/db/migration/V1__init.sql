-- V1__init.sql — Esquema inicial del sistema de turnos médicos
-- Ubicación: src/main/resources/db/migration/V1__init.sql

CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    phone_number varchar (30),
    is_active boolean not null DEFAULT true,
    created_at timestamptz not null DEFAULT now()
);

CREATE TABLE specialty (
    specialty_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) not null unique
);

CREATE TABLE location (
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) not null,
    address varchar(255) not null
);

CREATE TABLE doctor (
    doctor_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT not null unique REFERENCES users (user_id),
    license_number varchar(50) not null unique
);

CREATE TABLE patient (
    patient_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT not null unique REFERENCES users (user_id),
    health_insurance varchar(50)
);

CREATE TABLE user_role (
    user_id  BIGINT NOT NULL REFERENCES users (user_id),
    role VARCHAR(20) NOT NULL
    CHECK (role IN ('PATIENT', 'DOCTOR', 'RECEPTIONIST', 'ADMIN')),
    PRIMARY KEY (user_id, role)
);

CREATE TABLE consultation_type (
    consultation_type_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    specialty_id BIGINT not null REFERENCES specialty (specialty_id),
    name varchar(100) not null,
    duration_minutes int not null CHECK (duration_minutes > 0),
    UNIQUE (specialty_id, name)
);

CREATE TABLE doctor_specialty (
    doctor_id BIGINT not null REFERENCES doctor (doctor_id),
    specialty_id BIGINT not null REFERENCES specialty (specialty_id),
    PRIMARY KEY (doctor_id, specialty_id)
);

CREATE TABLE weekly_schedule (
    weekly_schedule_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    doctor_id BIGINT not null REFERENCES doctor (doctor_id),
    location_id BIGINT not null REFERENCES location (location_id),
    day_of_week varchar(20) NOT NULL
    CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY',
            'FRIDAY', 'SATURDAY', 'SUNDAY')),
    start_time time not null,
    end_time time not null
    CHECK (end_time > start_time),
    EXCLUDE USING gist (
        doctor_id   WITH =,
        day_of_week WITH =,
        tsrange(DATE '2000-01-01' + start_time,
        DATE '2000-01-01' + end_time, '[)') WITH &&
    )
);

CREATE TABLE schedule_exception (
    schedule_exception_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    scope varchar(20) not null
    CHECK (scope IN ('GENERAL', 'LOCATION', 'DOCTOR')),
    doctor_id BIGINT REFERENCES doctor (doctor_id),
    location_id BIGINT REFERENCES location (location_id),
    start_at timestamp not null,
    end_at timestamp not null
    CHECK (end_at > start_at),
    reason varchar(500) not null,
    created_by BIGINT not null REFERENCES users (user_id),
    created_at timestamptz not null DEFAULT now(),
    CHECK (
    (scope = 'GENERAL'  AND doctor_id IS NULL     AND location_id IS NULL) OR
    (scope = 'LOCATION' AND doctor_id IS NULL     AND location_id IS NOT NULL) OR
    (scope = 'DOCTOR'   AND doctor_id IS NOT NULL AND location_id IS NULL)
    )
);

CREATE TABLE appointment (
    appointment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id BIGINT not null REFERENCES patient (patient_id),
    doctor_id BIGINT not null REFERENCES doctor (doctor_id),
    location_id BIGINT not null REFERENCES location (location_id),
    consultation_type_id BIGINT not null REFERENCES consultation_type (consultation_type_id),
    start_time timestamp not null,
    end_time timestamp not null
    CHECK (end_time > start_time),
    status varchar(20) not null DEFAULT 'CONFIRMED'
    CHECK (status IN ('CANCELLED', 'CONFIRMED', 'ATTENDED', 'NO_SHOW')),
    reason varchar(500),
    late_cancellation boolean not null DEFAULT false,
    rescheduled_from_id BIGINT REFERENCES appointment (appointment_id),
    created_at timestamptz not null DEFAULT now(),
    EXCLUDE USING gist (
        doctor_id WITH =,
        tsrange(start_time, end_time, '[)') WITH &&
    ) WHERE (status <> 'CANCELLED'),
    EXCLUDE USING gist (
        patient_id WITH =,
        tsrange(start_time, end_time, '[)') WITH &&
    ) WHERE (status <> 'CANCELLED')
);

CREATE TABLE appointment_status_history (
    appointment_status_history_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    appointment_id BIGINT not null REFERENCES appointment (appointment_id),
    previous_status varchar(20)
    CHECK (previous_status IN ('CANCELLED', 'CONFIRMED', 'ATTENDED', 'NO_SHOW')),
    new_status varchar(20) not null
    CHECK (new_status IN ('CANCELLED', 'CONFIRMED', 'ATTENDED', 'NO_SHOW')),
    changed_by BIGINT REFERENCES users (user_id),
    changed_at timestamptz not null DEFAULT now(),
    reason varchar(500)
);

CREATE INDEX idx_status_history_appointment ON appointment_status_history (appointment_id);
CREATE INDEX idx_appointment_location       ON appointment (location_id);
CREATE INDEX idx_consultation_type_specialty ON consultation_type (specialty_id);