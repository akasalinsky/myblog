# YPblog — Блог на Spring Framework

Это веб-приложение реализует базовый блог с возможностью добавления, просмотра, редактирования и удаления постов и комментариев.
Приложение написано на **Java 21** с использованием **Spring Framework 6.2+**, **Thymeleaf**, **H2 Database** и **JUnit 5 + Mockito**.

---

## 🧰 Технологии

| Технология | Версия |
|------------|--------|
| Java       | 21     |
| Spring Framework | 6.2.1 |
| Thymeleaf  | 3.1.2.RELEASE |
| H2 Database | 2.2.224 |
| Maven      | 3.x    |
| JUnit 5 + Mockito | — |
| Servlet API (Jakarta) | 6.0.0 |

---

## 📦 Архитектура проекта

Проект разделён на слои согласно принципам разработки enterprise-приложений:

- **Model** — бизнес-объекты (`Post`, `Comment`)
- **DTO** — объекты передачи данных (`PostDTO`, `CommentDTO`)
- **Repository** — доступ к данным (`PostsRepository`, `CommentsRepository`)
- **Service** — бизнес-логика (`PostService`, `CommentService`)
- **Controller** — обработка HTTP-запросов (`PostsController`)
- **View** — HTML-страницы (в папке `templates`)

---

## 🚀 Как собрать проект

Для сборки проекта используется Maven:

```bash
mvn clean package