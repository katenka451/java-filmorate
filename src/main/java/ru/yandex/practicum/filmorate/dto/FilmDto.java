package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilmDto {
    private Long id;

    @NotNull
    @NotBlank
    private String name;
    private String description;
    private String releaseDate;
    private Integer duration;
    private MPADto mpa;
    private List<GenreDto> genres;
}