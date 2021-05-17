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
4. [Доступ](#Доступ)
5. [Описание методов API](#Описание-методов-API)
6. [Запросы API](#Запросы-API)
7. [Чат](#Чат)
8. [Описание методов чата](#Описание-методов-чата)
9. [Запросы чата](#Запросы-чата)

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

# Доступ
Для доступа в админку использовать следующие данные:  
**почта:** admin@gmail.com  
**пароль:** password  

# Описание методов API
URL                                                                                |Тип     |Описание                                   |
-----------------------------------------------------------------------------------|--------|-------------------------------------------|
[api/auth/sign-in](#api/auth/sign-in-(POST))                                       |POST    |Авторизация пользователя                   |
[api/auth/sign-up](#api/auth/sign-up-(POST))                                       |POST    |Регистрация пользователя                   |
[api/auth/accountVerification/{token}](#api/auth/accountVerification/{token}-(GET))|GET     |Подтверждение регистрации пользователя     |
[api/auth/forgot](#api/auth/forgot-(GET))                                          |GET     |Отправка токена по восстановлению пароля   |  
[api/auth/recovery](#api/auth/recovery-(GET))                                      |GET     |Восстановление пароля                      | 
[api/user/info](#api/user/info-(GET))                                              |GET     |Получить информацию о пользователе         |
[api/user](#api/user-(GET))                                                        |GET     |Получить всех пользователей                |
[api/user/password](#api/user/password-(PUT))                                      |PUT     |Редактирование пароля                      |
[api/user/email](#api/user/email-(PUT))                                            |PUT     |Редактирование почты                       |
[api/user/email/{email}/{token}](#api/user/email/{email}/{token}-(GET))            |GET     |Подтверждение смены почты                  |
[api/user/name](#api/user/name-(PUT))                                              |PUT     |Редактирование имени                       |
[api/user](#api/user-(DELETE))                                                     |DELETE  |Удалить пользователя                       |
[api/user/recipient](#api/user/recipient-(GET))                                    |GET     |Получить всех собеседников                 |
[api/user/recipient](#api/user/recipient-(POST))                                   |POST    |Добавить собеседника                       |
[api/order](#api/order-(POST))                                                     |POST    |Создание объявления                        |
[api/order](#api/order-(GET))                                                      |GET     |Получить все объявления                    |
[api/order](#api/order-(PUT))                                                      |PUT     |Редактирование объявления                  |
[api/order](#api/order-(DELETE))                                                   |DELETE  |Удалить объявление                         |
[api/order/complete](#api/order/complete-(GET))                                    |GET     |Завершить обновление                       |
[api/order/accept](#api/order/accept-(GET))                                        |GET     |Одобрить объявление                        |
[api/order/reject](#api/order/reject-(GET))                                        |GET     |Отклонить объявление                       |
[api/order/info](#api/order/info-(GET))                                            |GET     |Получить информацию об объявлении          |
[api/image/upload](#api/image/upload-(POST))                                       |POST    |Загрузка изображений на сервер             |
[api/image/{filename:.+}](#api/image/{filename:.+}-(GET))                          |GET     |Получение изображения                      |
[api/feedback](#api/feedback-(POST))                                               |POST    |Добавить отзыв пользователю                |
[api/feedback](#api/feedback-(DELETE))                                             |DELETE  |Удалить отзыв                              |
[api/admin](#api/admin-(POST))                                                     |POST    |Создать администратора                     |
[api/admin](#api/admin-(GET))                                                      |GET     |Получить всех администраторов              |


# Запросы API
## Аутентификация
### api/auth/sign-in (POST) <a name="api/auth/sign-in-(POST)"></a>
Авторизация пользователя
* Тело: 
    * **email** (_строка_) - логин пользователя
    * **password** (_строка_) - пароль пользователя
* Результат: 
    * **userId** (_положительное число_) - идентификатор пользователя
    * **accessToken** (_строка_) - токен для доступа
    * **role** (_строка_) - роль пользователя
    
### api/auth/sign-up (POST) <a name="api/auth/sign-up-(POST)"></a>
Регистрация пользователя
* Тело: 
    * **email** (_строка_) - логин пользователя
    * **password** (_строка_) - пароль пользователя
    * **name** (_строка_) - имя пользователя
* Результат: После успешного выполнения возвращает 1.

### api/auth/accountVerification/{token} (GET) <a name="api/auth/accountVerification/{token}-(GET)"></a>
Подтверждение регистрации пользователя
* Параметры: 
    * **token** (_строка_) - токен для активации аккаунта
* Результат: После успешного выполнения возвращает 1.

### api/auth/forgot (GET) <a name="api/auth/forgot-(GET)"></a>
Отправить токен для восстановления пароля
* Параметры: 
    * **email** (_строка_) - почта пользователя
* Результат: После успешного выполнения возвращает 1.

### api/auth/recovery (GET) <a name="api/auth/recovery-(GET)"></a>
Восстановление пароля
* Параметры: 
    * **token** (_строка_) - токен для восстановления аккаунта
    * **password** (_строка_) - новый пароль пользователя
* Результат: После успешного выполнения возвращает 1.

## Пользователи
### api/user/info (GET) <a name="api/user/info-(GET)"></a>
Получить информацию о пользователе
* Параметры: 
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает объект, описывающий пользователя.

### api/user/feedback (GET) <a name="api/user/feedback-(GET)"></a>
Получить все отзывы пользователя
* Параметры: 
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает массив объектов, описывающих отзыв.
* _Примечание_: Доступно только для роли USER

### api/user (GET) <a name="api/user-(GET)"></a>
Получить всех пользователей
* Параметры для фильтрации:
    * **email** (_строка_) - почта пользователя
    * **name** (_строка_) - имя пользователя
    * **role** (_строка_) - роль пользователя:
        * _USER_
* Параметры для сортировки:
    * **rating** (_положительное число_) - оценка пользователя
* Результат: После успешного выполнения возвращает массив объектов, описывающих пользователей.
* _Примечание_: Доступно только для роли ADMIN, SUPER_ADMIN

### api/user/password (PUT) <a name="api/user/password-(PUT)"></a>
Редактирование пароля
* Тело: 
    * **oldPassword** (_строка_) - старый пароль пользователя
    * **newPassword** (_строка_) - новый пароль пользователя
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает 1.

### api/user/email (PUT) <a name="api/user/email-(PUT)"></a>
Редактирование почты
* Параметры: 
    * **email** (_строка_) - новая почта пользователя
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

### api/user/email/{email}/{token} (GET) <a name="api/user/email-(PUT)"></a>
Подтверждение смены почты
* Параметры:
    * **email** (_строка_) - новая почта пользователя
    * **token** (_строка_) - токен для подтверждения смены почты
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

### api/user/name (PUT) <a name="api/user/name-(PUT)"></a>
Редактирование имени
* Параметры: 
    * **name** (_строка_) - имя пользователя
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

### api/user (DELETE) <a name="api/user-(DELETE)"></a>
Удалить пользователя
* Параметры: 
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

### api/user/recipient (GET) <a name="api/user/recipient-(GET)"></a>
Получить всех собеседников
* Параметры: 
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает массив объектов, описывающих пользователей.

### api/user/recipient (POST) <a name="api/user/recipient-(POST)"></a>
Добавить собеседника
* Тело: 
    * **userId** (_положительное число_) - идентификатор пользователя - отправителя
    * **recipientId** (_положительное число_) - идентификатор пользователя - получателя
    * **orderId** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает массив объектов, описывающих пользователей.

## Объявления
### api/order (POST) <a name="api/order-(POST)"></a>
Создание объявления
* Тело: 
    * **title** (_строка_) - название объявления
    * **description** (_строка_) - описание объявления
    * **type** (_строка_) - тип объявления
    * **addressSender** (_строка_) - адрес отправления
    * **addressRecipient** (_строка_) - адрес получение
    * **price** (_положительное число_) - цена
    * **departDate** (_строка_) - дата отправления
    * **arrivalDate** (_строка_) - дата получения
    * **orderSize** (_объект_) - объект с размерами:
        *  **width** (_положительное число_) - ширина
        *  **height** (_положительное число_) - высота
        *  **length** (_положительное число_) - длина
        *  **weight** (_положительное число_) - вес
    * **imagesId** (_строка_) - массив идентификаторов изображений.
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

### api/order (GET) <a name="api/order-(GET)"></a>
Получить все объявления
* Параметры для фильтрации:
  * **title** (_строка_) - название объявления
  * **type** (_строка_) - тип объявления:
    * _CARGO_
    * _CARRIER_
  * **addressSender** (_строка_) - адрес отправления
  * **addressRecipient** (_строка_) - адрес получение
  * **price** (_положительное число_) - цена
  * **departDate** (_строка_) - дата отправления
  * **arrivalDate** (_строка_) - дата получения
  * **width** (_положительное число_) - ширина
  * **height** (_положительное число_) - высота
  * **length** (_положительное число_) - длина
  * **weight** (_положительное число_) - вес
  * **status** (_строка_) - статус объявления:  
    * _CHECK_ 
    * _INACTIVE_ 
    * _ACTIVE_
    * _REFUSE_
* Параметры для сортировки:
  * **departDate** (_строка_) - дата отправления
  * **arrivalDate** (_строка_) - дата получения
  * **orderSize.width** (_положительное число_) - ширина
  * **orderSize.height** (_положительное число_) - высота
  * **orderSize.length** (_положительное число_) - длина
  * **orderSize.weight** (_положительное число_) - вес
* Результат: После успешного выполнения возвращает массив объектов, описывающих объявление.
* Пример:  
```http request
http://localhost:8080/api/order?sort=departDate, asc&weight=12
```

### api/order/info (GET) <a name="api/order/info-(GET)"></a>
Получить информацию об объявлении
* Параметры: 
    * **id** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает объект, описывающий объявление.

### api/order (PUT) <a name="api/order-(PUT)"></a>
Редактирование объявления
* Тело: 
    * **title** (_строка_) - название объявления
    * **description** (_строка_) - описание объявления
    * **type** (_строка_) - тип объявления
    * **addressSender** (_строка_) - адрес отправления
    * **addressRecipient** (_строка_) - адрес получение
    * **price** (_положительное число_) - цена
    * **departDate** (_строка_) - дата отправления
    * **arrivalDate** (_строка_) - дата получения
    * **orderSize** (_объект_) - объект с размерами:
        *  **width** (_положительное число_) - ширина
        *  **height** (_положительное число_) - высота
        *  **length** (_положительное число_) - длина
        *  **weight** (_положительное число_) - вес
    * **imagesId** (_строка_) - массив идентификаторов изображений.
* Параметры:     
    * **id** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает 1.

### api/order/complete (GET) <a name="api/order/complete-(GET)"></a>
Завершить объявление
* Параметры:
    * **id** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает 1.

### api/order/accept (GET) <a name="api/order/accept-(GET)"></a>
Одобрить объявление
* Параметры:
    * **id** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли ADMIN, SUPER_ADMIN

### api/order/reject (GET) <a name="api/order/reject-(GET)"></a>
Отклонить объявление
* Параметры:
    * **id** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли ADMIN, SUPER_ADMIN

### api/order (DELETE) <a name="api/order-(DELETE)"></a>
Удалить объявление
* Параметры: 
    * **id** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

## Изображения
### api/image/upload (POST) <a name="api/image/upload-(POST)"></a>
Загрузка изображений на сервер
* Параметры: 
    * **files** (_массив_) - массив изображений
* Результат: После успешного выполнения возвращает массив идентификаторов изображений.
* _Примечание_: Доступно только для роли USER

### api/image/{filename:.+} (GET) <a name="api/image/{filename:.+}-(GET)"></a>
Получение изображения
* Параметры: 
    * **filename** (_строка_) - название файла с расширением
* Результат: После успешного выполнения возвращает изображение.

## Отзывы  
Добавить отзыв пользователю
### api/feedback (POST) <a name="api/feedback-(POST)"></a>
* Тело
    * **authorName** (_строка_) - имя автора
    * **content** (_строка_) - отзыв  
    * **authorId** (_положительное число_) - идентификатор автора
    * **rating** (_положительное число_) - оценка пользователя  
    * **userId** (_положительное число_) - идентификатор пользователя
    * **orderId** (_положительное число_) - идентификатор объявления
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

### api/feedback (DELETE) <a name="api/feedback-(DELETE)"></a>
Удалить отзыв
* Параметры:
    * **id** (_положительное число_) - идентификатор отзыва
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли USER

## Администраторы
### api/admin (POST) <a name="api/admin-(POST)"></a>
Создание администратора
* Тело:
    * **email** (_строка_) - логин пользователя
    * **password** (_строка_) - пароль пользователя
* Результат: После успешного выполнения возвращает 1.
* _Примечание_: Доступно только для роли SUPER_ADMIN

### api/admin (GET) <a name="api/admin-(GET)"></a>
Получение всех администратора
* Параметры для фильтрации:
    * **email** (_строка_) - почта администратора
    * **role** (_строка_) - роль пользователя:
        * _ADMIN_
        * _SUPER_ADMIN_
* Результат: После успешного выполнения возвращает массив объектов, описывающих администраторов.
* _Примечание_: Доступно только для роли SUPER_ADMIN

# Чат
Для подключения к чату: 
```javascript
  const connect = () => {
    const Stomp = require("stompjs");
    var SockJS = require("sockjs-client");
    SockJS = new SockJS("http://localhost:8081/ws");
    stompClient = Stomp.over(SockJS);
    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    stompClient.subscribe(
      "/user/" + currentUser.userId + "/queue/messages",
      onMessageReceived
    );
  };

  const onError = (err) => {
    console.log(err);
  };
```

# Описание методов чата
URL                                                                                         |Тип     |Описание                                     |
--------------------------------------------------------------------------------------------|--------|---------------------------------------------|
[/messages/{senderId}/{recipientId}/count](#/messages/{senderId}/{recipientId}/count-(GET)) | GET    | Получить количество непрочитанных сообщений |
[/messages/{senderId}/{recipientId}](#/messages/{senderId}/{recipientId}-(GET))             | GET    | Получить все сообщения                      |
[/messages/{id}](#/messages/{id}-(GET))                                                     | GET    | Найти сообщение                             |

# Запросы чата
### /messages/{senderId}/{recipientId}/count (GET) <a name="/messages/{senderId}/{recipientId}/count-(GET)"></a>
Получить количество непрочитанных сообщений
* Параметры:
    * **senderId** (_положительное число_) - идентификатор отправителя
    * **recipientId** (_положительное число_) - идентификатор получателя
* Результат: После успешного выполнения положительное число.

### /messages/{senderId}/{recipientId} (GET) <a name="/messages/{senderId}/{recipientId}-(GET)"></a>
Получить все сообщения
* Параметры:
    * **senderId** (_положительное число_) - идентификатор отправителя
    * **recipientId** (_положительное число_) - идентификатор получателя
* Результат: После успешного выполнения возвращает массив объектов, описывающих сообщения.

### /messages/{id} (GET) <a name="/messages/{id}-(GET)"></a>
Найти сообщение
* Параметры:
    * **id** (_положительное число_) - идентификатор сообщения
* Результат: После успешного выполнения возвращает объект, описывающий сообщение.