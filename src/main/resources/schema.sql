-- Дополнительные констрейнты для обеспечения целостности данных

-- Удаляем споты без типа (если они есть)
DELETE FROM spots WHERE spot_type_id IS NULL;

-- Уникальный индекс для названия типа спота
CREATE UNIQUE INDEX IF NOT EXISTS uk_spot_types_name ON spot_types(name);

-- Индекс для быстрого поиска спотов по типу
CREATE INDEX IF NOT EXISTS idx_spots_spot_type_id ON spots(spot_type_id);

-- Индекс для поиска по городу
CREATE INDEX IF NOT EXISTS idx_spots_city ON spots(city);

-- Индекс для поиска по названию
CREATE INDEX IF NOT EXISTS idx_spots_name ON spots(name);

-- Проверочные констрейнты (создаются только если не существуют)
DO $$
BEGIN
    -- Проверочный констрейнт для цены (должна быть положительной)
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'chk_spots_price_positive') THEN
        ALTER TABLE spots ADD CONSTRAINT chk_spots_price_positive CHECK (price > 0);
    END IF;
    
    -- Проверочный констрейнт для координат (широта должна быть в диапазоне -90 до 90)
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'chk_spots_latitude_range') THEN
        ALTER TABLE spots ADD CONSTRAINT chk_spots_latitude_range CHECK (latitude >= -90 AND latitude <= 90);
    END IF;
    
    -- Проверочный констрейнт для координат (долгота должна быть в диапазоне -180 до 180)
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'chk_spots_longitude_range') THEN
        ALTER TABLE spots ADD CONSTRAINT chk_spots_longitude_range CHECK (longitude >= -180 AND longitude <= 180);
    END IF;
    
    -- Проверочный констрейнт для длины названия спота
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'chk_spots_name_length') THEN
        ALTER TABLE spots ADD CONSTRAINT chk_spots_name_length CHECK (length(name) >= 1 AND length(name) <= 255);
    END IF;
    
    -- Проверочный констрейнт для длины описания спота
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'chk_spots_description_length') THEN
        ALTER TABLE spots ADD CONSTRAINT chk_spots_description_length CHECK (length(description) >= 1 AND length(description) <= 1000);
    END IF;
    
    -- Проверочный констрейнт для длины названия типа спота
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'chk_spot_types_name_length') THEN
        ALTER TABLE spot_types ADD CONSTRAINT chk_spot_types_name_length CHECK (length(name) >= 1 AND length(name) <= 255);
    END IF;
END $$; 