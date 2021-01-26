# Для запуска приложения

1. Стартуем постгресс - docker-compose up -d
2. Компилируем - mvn clean install
3. Запускаем - mvn spring-boot:run

# Юнит и Интеграционные тесты по всем юзкейсам: 

src/test/java/ru/awg/rupost/demo/

# Примененные технологии согласно ТЗ:

1. Транспорт: Grps
2. Контейнер сервлетов: Undertow
3. БД: Postgres от 9.5 и выше
4. ORM: Hibernate
5. Другие библиотеки и фреймворки: Liquibase, Spring, JUnit
