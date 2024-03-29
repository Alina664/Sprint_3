Автотесты на API учебного сервиса: http://qa-scooter.praktikum-services.ru/

Его документация: https://qa-scooter.praktikum-services.ru/docs/

Подготовка:
  - Создан Maven-проект.
  - Подключены JUnit 4, RestAssured, Allure.

Протестированы ручки
  - Создание курьера
    - курьера можно создать;
    - нельзя создать двух одинаковых курьеров;
    - чтобы создать курьера, нужно передать в ручку все обязательные поля;
    - запрос возвращает правильный код ответа;
    - успешный запрос возвращает ok: true;
    - если одного из полей нет, запрос возвращает ошибку;
    - если создать пользователя с логином, который уже есть, возвращается ошибка.
  
  - Логин курьера
    - курьер может авторизоваться;
    - для авторизации нужно передать все обязательные поля;
    - система вернёт ошибку, если неправильно указать логин или пароль;
    - если какого-то поля нет, запрос возвращает ошибку;
    - если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    - успешный запрос возвращает id.

  - Создание заказа
    - можно указать один из цветов — BLACK или GREY;
    - можно указать оба цвета;
    - можно совсем не указывать цвет;
    - тело ответа содержит track.
    - использование параметризации для создания заказа.
  - Список заказов
    - в тело ответа возвращается список заказов.

Отчёт Allure
