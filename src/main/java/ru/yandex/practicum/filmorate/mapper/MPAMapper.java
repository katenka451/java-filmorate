package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.model.MPA;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MPAMapper {

    public static MPADto mapToMPADto(MPA mpa) {
        return MPADto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public static MPA mapDtoToMPA(MPADto mpaDto) {
        return MPA.builder()
                .id(mpaDto.getId())
                .name(mpaDto.getName())
                .build();
    }
}
