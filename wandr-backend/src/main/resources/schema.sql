CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trips (
    trip_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    city VARCHAR(255),
    start_date DATE,
    end_date DATE,
    description TEXT,
    cover VARCHAR(500),
    planned_budget DECIMAL(10, 2),
    status VARCHAR(50) DEFAULT 'planned' CHECK (status IN ('planned', 'ongoing', 'completed')),
    rating DECIMAL(3, 1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS expenses_categories (
    expense_category_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    icon VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS memories (
    memory_id SERIAL PRIMARY KEY,
    trip_id INTEGER NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(50) CHECK (type IN ('meal', 'activity', 'accommodation', 'place')),
    description TEXT,
    visited_at DATE,
    address VARCHAR(500),
    is_favorite BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS photos (
    photo_id SERIAL PRIMARY KEY,
    memory_id INTEGER NOT NULL REFERENCES memories(memory_id) ON DELETE CASCADE,
    url VARCHAR(500) NOT NULL,
    caption VARCHAR(500),
    taken_at TIMESTAMP,
    position INTEGER
);

CREATE TABLE IF NOT EXISTS expenses (
    expense_id SERIAL PRIMARY KEY,
    trip_id INTEGER NOT NULL REFERENCES trips(trip_id) ON DELETE CASCADE,
    memory_id INTEGER REFERENCES memories(memory_id) ON DELETE SET NULL,
    expense_category_id INTEGER NOT NULL REFERENCES expenses_categories(expense_category_id),
    label VARCHAR(255),
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'EUR',
    spent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ratings (
    rating_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    memory_id INTEGER NOT NULL REFERENCES memories(memory_id) ON DELETE CASCADE,
    value_for_money DECIMAL(2, 1),
    atmosphere_and_service DECIMAL(2, 1),
    quality_of_activity DECIMAL(2, 1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tags (
    tag_id SERIAL PRIMARY KEY,
    memory_id INTEGER NOT NULL REFERENCES memories(memory_id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(7),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
