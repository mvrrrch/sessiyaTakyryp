# sessiyaTakyryp

User (пользователь)
- id (PK)
- username
- email
- password_hash
- created_at

Task (задача)
- id (PK)
- title
- description
- completed
- user_id (FK → User.id)
- created_at
  Связь:
  Один User может иметь много Task (1:M)

Каждая Task принадлежит одному User

CREATE TABLE users (
id SERIAL PRIMARY KEY,
username VARCHAR(50) UNIQUE NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
password_hash TEXT NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
id SERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
description TEXT,
completed BOOLEAN DEFAULT FALSE,
user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


![Снимок экрана 2025-05-26 в 00.04.56.png](../../Desktop/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA%20%D1%8D%D0%BA%D1%80%D0%B0%D0%BD%D0%B0%202025-05-26%20%D0%B2%2000.04.56.png)