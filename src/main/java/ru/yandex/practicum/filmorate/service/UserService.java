package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendService friendService;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return userRepository.getById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserDto create(UserDto newUser) {
        validateDataCreation(newUser);
        return UserMapper.mapToUserDto(userRepository.create(UserMapper.mapDtoToUser(newUser)));
    }

    public UserDto update(UserDto newUser) {
        validateDataUpdate(newUser);
        return UserMapper.mapToUserDto(userRepository.update(UserMapper.mapDtoToUser(newUser)));
    }

    public boolean delete(Long id) {
        return userRepository.delete(id);
    }

    public boolean addToFriends(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            String errorText = "Пользователь не может добавить в друзья сам себя";
            log.error(errorText);
            throw new ValidationException(errorText);
        }

        return friendService.create(getById(userId).getId(), getById(friendId).getId());
    }

    public boolean deleteFromFriends(Long userId, Long friendId) {
        return friendService.delete(getById(userId).getId(), getById(friendId).getId());
    }

    public List<UserDto> getUserFriends(Long userId) {
        return userRepository.findAllFriends(getById(userId).getId()).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getCommonFriends(Long userId1, Long userId2) {
        return userRepository.findCommonFriends(getById(userId1).getId(), getById(userId2).getId()).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private void validateDataCreation(UserDto user) {
        log.info("Вызвана операция создания пользователя {}", user.getLogin());
        validateLogin(user.getLogin());
        validateBirthday(LocalDate.parse(user.getBirthday()));

        if (userRepository.isEmailUsed(user.getEmail())) {
            throw new ValidationException("Этот e-mail уже используется");
        }
    }

    private void validateDataUpdate(UserDto user) {
        log.info("Вызвана операция обновления пользователя {}", user.getLogin());

        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (getById(user.getId()) == null) {
            throw new UserNotFoundException(user.getId());
        }
        if (user.getLogin() != null && !user.getLogin().isEmpty()) {
            validateLogin(user.getLogin());
        }
        if (user.getBirthday() != null && !user.getBirthday().isEmpty()) {
            validateBirthday(LocalDate.parse(user.getBirthday()));
        }

        if (userRepository.isEmailUsed(user.getEmail())) {
            throw new ValidationException("Этот e-mail уже используется");
        }
    }

    private void validateLogin(String login) {
        if (login.contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }

    private void validateBirthday(LocalDate birthday) {
        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC);
        if (today.isBefore(birthday)) {
            throw new ValidationException("Дата рождения должна быть в прошлом");
        }
    }

}
