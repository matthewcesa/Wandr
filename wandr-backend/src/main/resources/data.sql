INSERT INTO expenses_categories (name, icon)
VALUES
    ('Meal', '🍽️'),
    ('Activity', '🎯'),
    ('Transportation', '🚗'),
    ('Accommodation', '🏨')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (name, email, created_at, updated_at)
VALUES ('Matthew Cesa', 'matthewcesa@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

INSERT INTO trips (
        user_id, title, country, city, start_date, end_date, description,
        cover, planned_budget, status, rating, created_at, updated_at
)
SELECT
        u.user_id,
        'Voyage de test a Lisbonne',
        'Portugal',
        'Lisbonne',
        DATE '2026-06-12',
        DATE '2026-06-18',
        'Voyage de demonstration pour tester les endpoints Wandr.',
        '/images/lisbonne.jpg',
        850.00,
        'planned',
        4.5,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
FROM users u
WHERE u.email = 'matthewcesa@gmail.com'
    AND NOT EXISTS (
            SELECT 1 FROM trips t
            WHERE t.user_id = u.user_id
                AND t.title = 'Voyage de test a Lisbonne'
    );

INSERT INTO memories (
        trip_id, title, type, description, visited_at, address,
        is_favorite, created_at, updated_at
)
SELECT
        t.trip_id,
        'Tram 28 et Alfama',
        'place',
        'Balade dans les rues historiques de Lisbonne.',
        DATE '2026-06-13',
        'Alfama, Lisbonne, Portugal',
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
FROM trips t
JOIN users u ON u.user_id = t.user_id
WHERE u.email = 'matthewcesa@gmail.com'
    AND t.title = 'Voyage de test a Lisbonne'
    AND NOT EXISTS (
            SELECT 1 FROM memories m
            WHERE m.trip_id = t.trip_id
                AND m.title = 'Tram 28 et Alfama'
    );

INSERT INTO photos (memory_id, url, caption, taken_at, position)
SELECT
        m.memory_id,
        '/images/alfama.jpg',
        'Les rues de l Alfama',
        TIMESTAMP '2026-06-13 14:30:00',
        1
FROM memories m
JOIN trips t ON t.trip_id = m.trip_id
JOIN users u ON u.user_id = t.user_id
WHERE u.email = 'matthewcesa@gmail.com'
    AND m.title = 'Tram 28 et Alfama'
    AND NOT EXISTS (
            SELECT 1 FROM photos p
            WHERE p.memory_id = m.memory_id
                AND p.position = 1
    );

INSERT INTO tags (memory_id, name, color, created_at)
SELECT
        m.memory_id,
        'culture',
        '#E76F51',
        CURRENT_TIMESTAMP
FROM memories m
JOIN trips t ON t.trip_id = m.trip_id
JOIN users u ON u.user_id = t.user_id
WHERE u.email = 'matthewcesa@gmail.com'
    AND m.title = 'Tram 28 et Alfama'
    AND NOT EXISTS (
            SELECT 1 FROM tags tag
            WHERE tag.memory_id = m.memory_id
                AND tag.name = 'culture'
    );

INSERT INTO ratings (
        user_id, memory_id, value_for_money, atmosphere_and_service,
        quality_of_activity, created_at, updated_at
)
SELECT
        u.user_id,
        m.memory_id,
        4.5,
        5.0,
        4.5,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
FROM users u
JOIN trips t ON t.user_id = u.user_id
JOIN memories m ON m.trip_id = t.trip_id
WHERE u.email = 'matthewcesa@gmail.com'
    AND m.title = 'Tram 28 et Alfama'
    AND NOT EXISTS (
            SELECT 1 FROM ratings r
            WHERE r.user_id = u.user_id
                AND r.memory_id = m.memory_id
    );

INSERT INTO expenses (
        trip_id, memory_id, expense_category_id, label, amount,
        currency, spent_at, created_at
)
SELECT
        t.trip_id,
        m.memory_id,
        c.expense_category_id,
        'Billet de tram',
        8.00,
        'EUR',
        TIMESTAMP '2026-06-13 14:00:00',
        CURRENT_TIMESTAMP
FROM trips t
JOIN users u ON u.user_id = t.user_id
JOIN memories m ON m.trip_id = t.trip_id
JOIN expenses_categories c ON c.name = 'Transportation'
WHERE u.email = 'matthewcesa@gmail.com'
    AND t.title = 'Voyage de test a Lisbonne'
    AND m.title = 'Tram 28 et Alfama'
    AND NOT EXISTS (
            SELECT 1 FROM expenses e
            WHERE e.trip_id = t.trip_id
                AND e.label = 'Billet de tram'
    );
