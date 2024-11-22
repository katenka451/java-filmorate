package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public boolean create(Long userId, Long friendId) {
        return friendRepository.create(userId, friendId);
    }

    public boolean delete(Long userId, Long friendId) {
        return friendRepository.delete(userId, friendId);
    }

}
