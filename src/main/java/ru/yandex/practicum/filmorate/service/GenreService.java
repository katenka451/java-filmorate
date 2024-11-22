package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto getById(Long id) {
        return genreRepository.getById(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new GenreNotFoundException(id));
    }

    public List<GenreDto> getByFilmId(Long id) {
        return genreRepository.findByFilmId(id).stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto create(GenreDto newGenre) {
        return GenreMapper.mapToGenreDto(genreRepository.create(GenreMapper.mapDtoToGenre(newGenre)));
    }

    public boolean delete(Long id) {
        return genreRepository.delete(id);
    }
}
