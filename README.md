# REST API-сервис для создания объявления по грузоперевозкам
_REST API-сервис для создания объявления по грузоперевозкам_ — это интерфейс, который позволяет управлять объявлениями с помощью http-запросов к специальному серверу. 
Синтаксис запросов и тип возвращаемых ими данных строго определены на стороне самого сервиса.
Например, для получения информации о пользователе необходимо составить запрос такого вида:
```
http://localhost:8080/api/user/info?id=2
```

# Оглавление
1. [Запуск](#Запуск)
2. [Сборка](#Сборка)
3. [База данных](#База-данных)
4. [Документация](#Документация)

# Запуск
Для разработки запускать в конфигурации:
```
--spring.profiles.active=dev
```

# Сборка
```bash
mvn clean install
```

# База данных
Создать БД spring_cargo
```sql
mysql -u root -p
CREATE DATABASE spring_cargo DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
CREATE DATABASE spring_cargo_chat DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
```
Изменить поля в application-client.properties
```properties
spring.datasource.url = jdbc:mysql://[host]:[port]/spring_cargo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useLegacyDatetimeCode=false&characterEncoding=utf8
spring.datasource.username = [username]
spring.datasource.password = [password]
```

# Документация
Документация доступна по <a href="http://localhost:8080/api/swagger-ui.html#/">ссылке</a>