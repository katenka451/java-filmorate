package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MPARepository;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPAMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {

    private final MPARepository mpaRepository;

    public List<MPADto> findAll() {
        return mpaRepository.findAll().stream()
                .map(MPAMapper::mapToMPADto)
                .collect(Collectors.toList());
    }

    public MPADto getById(Long id) {
        return mpaRepository.getById(id)
                .map(MPAMapper::mapToMPADto)
                .orElseThrow(() -> new MPANotFoundException(id));
    }

    public MPADto create(MPADto newMPA) {
        return MPAMapper.mapToMPADto(mpaRepository.create(MPAMapper.mapDtoToMPA(newMPA)));
    }

    public boolean delete(Long id) {
        return mpaRepository.delete(id);
    }
}
