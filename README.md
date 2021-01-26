# rupost-demo

1. Стартуем постгресс - docker-compose up -d
2. Компилируем - mvn clean install
3. Запускаем - mvn spring-boot:run

 Юнит и Интеграционные тесты по всем юзкейсам: src/test/java/ru/awg/rupost/demo/

 Примененные технологии согласно ТЗ:

 Транспорт: Grps
 Контейнер сервлетов: Undertow
 БД: Postgres от 9.5 и выше
 ORM: Hibernate
 Другие библиотеки и фреймворки: Liquibase, Spring, JUnit
