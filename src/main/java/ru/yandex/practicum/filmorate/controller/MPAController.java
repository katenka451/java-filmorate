package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController {

    private final MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPADto> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public MPADto getById(@PathVariable Long id) {
        return mpaService.getById(id);
    }

}
