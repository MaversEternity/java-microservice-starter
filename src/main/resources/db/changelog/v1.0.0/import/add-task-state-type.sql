CREATE TYPE task_state AS ENUM (
    'NEW',
    'SCHEDULED',
    'CANCELLED',
    'IN_PROGRESS'
);