# Finance Manager Service

## Описание
Консольное приложение для управления личными финансами. Проект позволяет пользователям добавлять доходы и расходы, устанавливать бюджеты на категории и получать статистику по своим финансам.



Программа написана на Java и использует JSON для хранения данных.

---

## Требования

- **Java Development Kit (JDK)** версии 8 или выше
- **Файлы**:
    - `db.json`: база данных ссылок (создаётся автоматически)

---

## Установка

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/vasmarfas/FinanceManager
   cd FinanceManager
   ```

2. Откройте проект в IntelliJ IDEA или другой IDE, поддерживающей Gradle.

3. Убедитесь, что Gradle настроен в IDE (если требуется). Обычно IDE автоматически распознаёт файл `build.gradle.kts` и настраивает проект.

---

## Запуск

1. В IntelliJ IDEA выберите конфигурацию запуска `Main` и нажмите `Run`.

2. Следуйте инструкциям на экране, выбирая пункты меню и вводя данные по запросу.

---

## Использование

Программа предоставляет меню с выбором действий, например:
1. Добавить операцию
2. Управлять категориями
3. Вывести общую сумму доходов/расходов
4. Отобразить детализацию по всем операциям
5. Выполнить подсчёт по выбранным категориям
6. Перевести деньги другому пользователю

Выберите действие, следуйте инструкциям и вводите необходимые данные.

---

## Примеры использования

### Добавление операции
Введите тип операции, её категорию, сумму и операция будет добавлена. Её можно будет увидеть при выводе детализации по операциям.

### Перевод другому пользователю
Просто введите логин получателя и сумму перевода. Остальное программа сделает самостоятельно.

---

## Зависимости

Проект использует:
- **Gson** для работы с JSON
- **JSON-Simple** для обработки базы данных

---


## Контакты

Автор: vasmarfas

Telegram: [@vasmarfas](https://t.me/vasmarfas)

---
