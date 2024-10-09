package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    Long id;
    Set<Long> likes;

    @NotNull
    @NotBlank
    String name;

    String description;
    LocalDate releaseDate;
    Duration duration;

    public void addLike(Long id) {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        this.likes.add(id);
    }

    public void deleteLike(Long id) {
        if (this.likes == null) {
            return;
        }
        this.likes.remove(id);
    }
}
