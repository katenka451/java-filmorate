INSERT INTO users (email, login, name, birthday)
    VALUES ('Zena.Kutch@yahoo.com', '9TcjWsCiGZ',
            'Roberto Carroll', '2009-10-25');

INSERT INTO users (email, login, name, birthday)
VALUES ('Isac_Pacocha@yahoo.com', 'HVAmua7HHg',
        'Brooke Block', '1990-02-14');

INSERT INTO users (email, login, name, birthday)
VALUES ('Ubaldo_Lubowitz58@gmail.com', 'e5wFBNvjaY',
        'Alexandra Kuvalis', '1970-06-02');

INSERT INTO users (email, login, name, birthday)
VALUES ('Dax_Huel71@gmail.com', '4orq6ysx4S',
        'Claire Rodriguez', '1962-02-24');

INSERT INTO users (email, login, name, birthday)
VALUES ('Darrell.Wisoky@hotmail.com', 'Ft4xOcQR2J',
        'Darrell Wisoky', '1972-03-03');

INSERT INTO users (email, login, name, birthday)
VALUES ('Darrell.Warren@hotmail.com', 'WarrenQR2J',
        'Darrell Warren', '1970-01-03');


INSERT INTO friendship (user_id, friend_id)
VALUES (1,2);

INSERT INTO friendship (user_id, friend_id)
VALUES (1,3);

INSERT INTO friendship (user_id, friend_id)
VALUES (1,4);

INSERT INTO friendship (user_id, friend_id)
VALUES (1,5);

INSERT INTO friendship (user_id, friend_id)
VALUES (2,1);

INSERT INTO friendship (user_id, friend_id)
VALUES (2,3);

INSERT INTO friendship (user_id, friend_id)
VALUES (2,4);

INSERT INTO friendship (user_id, friend_id)
VALUES (3,1);

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Film_1', 'This is film 1',
        '1982-01-11', 172, 5);

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Film_2', 'This is film 2',
        '1980-07-27', 73, 4);

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Film_3', 'This is film 3',
        '1974-05-01', 98, 3);

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Film_4', 'This is film 4',
        '2009-07-12', 113, 1);

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Film_5', 'This is film 5',
        '2009-07-12', 102, 3);

INSERT INTO filmgenres (film_id, genre_id)
VALUES (1, 3);

INSERT INTO filmgenres (film_id, genre_id)
VALUES (1, 4);

INSERT INTO filmgenres (film_id, genre_id)
VALUES (2, 1);

INSERT INTO filmgenres (film_id, genre_id)
VALUES (3, 5);

INSERT INTO filmgenres (film_id, genre_id)
VALUES (4, 1);

INSERT INTO filmgenres (film_id, genre_id)
VALUES (4, 6);


INSERT INTO likes (film_id, user_id)
VALUES (1, 1);

INSERT INTO likes (film_id, user_id)
VALUES (1, 2);

INSERT INTO likes (film_id, user_id)
VALUES (1, 4);

INSERT INTO likes (film_id, user_id)
VALUES (1, 5);

INSERT INTO likes (film_id, user_id)
VALUES (2, 2);

INSERT INTO likes (film_id, user_id)
VALUES (3, 1);

INSERT INTO likes (film_id, user_id)
VALUES (3, 4);