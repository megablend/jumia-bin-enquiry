INSERT INTO users(id, created_at, email, password, updated_at, user_role) 
SELECT * FROM (SELECT 1, to_date('2019-02-09', 'YYYY-MM-DD'), 'noniboycharsy@gmail.com', '$2a$10$4NnQCfnhcSV9sU2tT/WFs.c5OmQ3kKfSTjWzHtyzraDOUKu/.cWnq', to_date('2019-02-09', 'YYYY-MM-DD'), 'user') AS tmp
WHERE NOT EXISTS (
    SELECT email FROM users WHERE email = 'noniboycharsy@gmail.com'
) LIMIT 1;

-- values(1, '2019-02-09', 'noniboycharsy@gmail.com', '$2a$10$MqGVkDquVsVZcdKKdvRcDugHJCgUrs/YLEFQZvyOITqhdklrflu/y', '2019-02-09', 'user');