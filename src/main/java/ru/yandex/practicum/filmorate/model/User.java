package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    Long id;
    Set<Long> friends;

    @NotNull
    @NotBlank
    @Email
    String email;

    @NotNull
    @NotBlank
    String login;

    String name;
    LocalDate birthday;

    public void addFriend(Long id) {
        if (this.friends == null) {
            this.friends = new HashSet<>();
        }
        this.friends.add(id);
    }

    public void deleteFriend(Long id) {
        if (this.friends == null) {
            return;
        }
        this.friends.remove(id);
    }
}
