-- Создание типов спотов
INSERT INTO spot_types (name, description) VALUES
('Скейтпарк', 'Места для катания на скейтборде'),
('Вейк-серф спот', 'Места для вейк-серфинга'),
('Сноуборд спот', 'Места для катания на сноуборде'),
('Фитнес-центр', 'Спортивные залы и фитнес-клубы'),
('Йога-студия', 'Места для занятий йогой'),
('Бассейн', 'Плавательные бассейны'),
('Теннисный корт', 'Корты для игры в теннис'),
('Футбольное поле', 'Поля для игры в футбол');

-- Добавление спотов с правильными spot_type_id
INSERT INTO spots (name, description, city, address, price, schedule, latitude, longitude, spot_type_id) VALUES
('Фитнес Парк', 'Современный фитнес-центр с полным набором тренажеров', 'Москва', 'ул. Примерная, 1', 500, 'Пн-Вс 8:00-22:00', 55.751244, 37.618423, (SELECT id FROM spot_types WHERE name = 'Фитнес-центр' LIMIT 1)),
('Yoga Place', 'Уютная студия для занятий йогой и медитацией', 'Санкт-Петербург', 'ул. Ленина, 10', 700, 'Пн-Пт 9:00-21:00', 59.9342802, 30.3350986, (SELECT id FROM spot_types WHERE name = 'Йога-студия' LIMIT 1)),
('Скейт-парк "Экстрим"', 'Большой скейт-парк с различными препятствиями', 'Москва', 'ул. Спортивная, 15', 300, 'Пн-Вс 9:00-22:00', 55.7558, 37.6176, (SELECT id FROM spot_types WHERE name = 'Скейтпарк' LIMIT 1)),
('Вейк-парк "Волна"', 'Профессиональный вейк-парк на воде', 'Санкт-Петербург', 'наб. Фонтанки, 25', 1200, 'Пн-Вс 10:00-20:00', 59.9343, 30.3351, (SELECT id FROM spot_types WHERE name = 'Вейк-серф спот' LIMIT 1)),
('Сноуборд-парк "Снег"', 'Трассы для сноуборда и лыж', 'Сочи', 'ул. Горная, 8', 800, 'Пн-Вс 8:00-18:00', 43.5855, 39.7231, (SELECT id FROM spot_types WHERE name = 'Сноуборд спот' LIMIT 1));

INSERT INTO instructors (name, specialization, experience, description, price, contacts) VALUES
('Иван Иванов', 'Йога', 5, 'Опытный тренер по йоге', 1000, '+79998887766'),
('Мария Смирнова', 'Пилатес', 3, 'Сертифицированный инструктор по пилатесу', 1200, '+79997776655');

-- Добавление фото для спотов (примеры URL)
INSERT INTO spot_photos (spot_id, photo_url, is_main, created_at) VALUES
((SELECT id FROM spots WHERE name = 'Фитнес Парк' LIMIT 1), 'https://example.com/fitness-park-main.jpg', true, CURRENT_TIMESTAMP),
((SELECT id FROM spots WHERE name = 'Фитнес Парк' LIMIT 1), 'https://example.com/fitness-park-1.jpg', false, CURRENT_TIMESTAMP),
((SELECT id FROM spots WHERE name = 'Фитнес Парк' LIMIT 1), 'https://example.com/fitness-park-2.jpg', false, CURRENT_TIMESTAMP),
((SELECT id FROM spots WHERE name = 'Yoga Place' LIMIT 1), 'https://example.com/yoga-place-main.jpg', true, CURRENT_TIMESTAMP),
((SELECT id FROM spots WHERE name = 'Скейт-парк "Экстрим"' LIMIT 1), 'https://example.com/skate-park-main.jpg', true, CURRENT_TIMESTAMP); 