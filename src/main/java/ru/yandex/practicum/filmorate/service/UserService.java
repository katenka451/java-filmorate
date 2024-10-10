package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public User create(User newUser) {
        return userStorage.create(newUser);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }

    public User addToFriends(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);

        if (userId.equals(friendId)) {
            String errorText = "Пользователь не может добавить в друзья сам себя";
            log.error(errorText);
            throw new ValidationException(errorText);
        }

        userStorage.getById(userId).addFriend(friendId);
        userStorage.getById(friendId).addFriend(userId);

        return userStorage.getById(userId);
    }

    public User deleteFromFriends(Long userId, Long notFriendAnymoreId) {
        checkUser(userId);
        checkUser(notFriendAnymoreId);

        userStorage.getById(userId).deleteFriend(notFriendAnymoreId);
        userStorage.getById(notFriendAnymoreId).deleteFriend(userId);

        return userStorage.getById(userId);
    }

    public List<User> getUserFriends(Long userId) {
        checkUser(userId);

        Set<Long> friendsList = userStorage.getById(userId).getFriends();
        if (friendsList == null) {
            return Collections.emptyList();
        }

        return userStorage.findAll().stream()
                .filter(user -> friendsList.contains(user.getId()))
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        checkUser(userId);
        checkUser(otherId);

        Set<Long> userFriendsList = userStorage.getById(userId).getFriends();
        Set<Long> otherFriendsList = userStorage.getById(otherId).getFriends();
        if (userFriendsList == null || otherFriendsList == null) {
            return Collections.emptyList();
        }

        userFriendsList.retainAll(otherFriendsList);

        return userStorage.findAll().stream()
                .filter(user -> userFriendsList.contains(user.getId()))
                .toList();
    }

    public void checkUser(Long userId) {
        if (!userStorage.exists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

}
