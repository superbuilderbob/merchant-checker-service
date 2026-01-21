SET schema 'public';

CREATE TABLE IF NOT EXISTS merchant (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mcc INT NOT NULL,
    description TEXT NOT NULL
--     mode_of_payment: enum,
--     created_at,
--     updated_at: timestamp,
)