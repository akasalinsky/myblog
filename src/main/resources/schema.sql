-- Таблица с постами
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    text TEXT,
    imagePath VARCHAR(512),
    likesCount BIGINT DEFAULT 0,
    tags VARCHAR(1024)
);

-- Таблица с комментариями
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);