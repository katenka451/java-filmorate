package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.LikeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public boolean create(Long filmId, Long userId) {
        return likeRepository.create(filmId, userId);
    }

    public boolean delete(Long filmId, Long userId) {
        return likeRepository.delete(filmId, userId);
    }
}
