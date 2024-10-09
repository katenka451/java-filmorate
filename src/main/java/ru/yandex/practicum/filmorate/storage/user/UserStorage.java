package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User create(User newUser);

    User update(User newUser);

    void delete(Long id);

    boolean exists(Long id);

    User getById(Long id);
}
