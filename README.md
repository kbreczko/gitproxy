# gitproxy

Prosty RESTowy serwis, który zwróca informacje: 
- Identyfikator 
- Login 
- Nazwa 
- Typ 
- Url do avatara 
- Data stworzenia 
- Obliczenia


Serwis pobiera dane z https://api.github.com/users/{login} (więcej informacji na [github rest api](https://docs.github.com/en/rest/reference/users))) i przekazuje je w API. 
W polu calculations zwracany jest wynik działania: \
`6 / liczba_followers * (2 + liczba_public_repos)` 

 
Serwis zapisuje w bazie danych liczbę wywołań dla każdego loginu. 
Liczba wywołań jest zwiększana dla każdego zapytania o 1.
