-- Сначала посты
INSERT INTO posts(title, text, imagePath, likesCount, tags)
VALUES ('One', 'OneText', '123.jpg', 1, 'First');
INSERT INTO posts(title, text, imagePath, likesCount, tags)
VALUES ('Two', 'TwoText', '124.jpg', 2, 'Second');
INSERT INTO posts(title, text, imagePath, likesCount, tags)
VALUES ('Three', 'ThreeText', '125.jpg', 3, 'Third');

-- Комментарии к посту 1
INSERT INTO comments(post_id, content) VALUES (1, 'Круто');
INSERT INTO comments(post_id, content) VALUES (1, 'Отстой');

-- Комментарии к посту 2
INSERT INTO comments(post_id, content) VALUES (2, 'Норм');
INSERT INTO comments(post_id, content) VALUES (2, 'Круто');
INSERT INTO comments(post_id, content) VALUES (2, 'Отстой');

-- Комментарии к посту 3
INSERT INTO comments(post_id, content) VALUES (3, 'Отстой');
INSERT INTO comments(post_id, content) VALUES (3, 'Норм');
INSERT INTO comments(post_id, content) VALUES (3, 'Круто');
