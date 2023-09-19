package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.service.CategoryService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public void createCategory(@RequestParam String name, @RequestParam @Pattern(regexp = "#\\d{6}") String color) {
        categoryService.createCategory(name, color);
    }

    @DeleteMapping
    public void deleteCategory(@RequestParam long id) {
        categoryService.deleteCategory(id);
    }
}
