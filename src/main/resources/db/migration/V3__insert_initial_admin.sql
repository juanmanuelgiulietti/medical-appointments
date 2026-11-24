INSERT INTO users (first_name, last_name, email, password_hash)
VALUES ('Admin', 'Test', 'admin@example.com', '$2a$10$pb.IpHWRsCfqcPMChpWFbeLOS8A1Sh6tjV.mSei6RJr8CEcfth9eS');

INSERT INTO user_role (user_id, role)
SELECT user_id, 'ADMIN'
FROM users
WHERE email = 'admin@example.com';