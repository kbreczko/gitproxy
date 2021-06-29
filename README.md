# gitproxy

Prosty RESTowy serwis odpytujący API githuba oraz zapisujący liczbę wywołań w bazie danych.

Wystawione endpointy:
- `GET` `/users/{login}`

Zwracany model:
- Identyfikator (`id`)
- Login (`login`)
- Nazwa (`name`)
- Typ (`type`)
- Url do avatara (`avatarUrl`) 
- Data stworzenia (`createdAt`)
- Obliczenia (`calculations`)


Szczegóły:

Serwis zapisuje w bazie danych liczbę wywołań dla każdego loginu, zwiększając wartość dla każdego zapytania o 1. 
Następnie, pobiera dane z https://api.github.com/users/{login} (więcej informacji na [rest_api](https://docs.github.com/en/rest/reference/users)) i przekazuje je w API serwisu. 
W polu `calculations` zwracany jest wynik działania: \
`6 / liczba_followers * (2 + liczba_public_repos)` z zaokrągleniem do 2 miejsc po przecinku.

 

