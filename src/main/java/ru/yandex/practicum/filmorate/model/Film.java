package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    Long id;

    @NotNull
    @NotBlank
    String name;

    String description;
    LocalDate releaseDate;
    Duration duration;
}
