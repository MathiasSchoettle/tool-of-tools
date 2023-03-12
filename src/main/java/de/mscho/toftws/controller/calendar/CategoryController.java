package de.mscho.toftws.controller.calendar;

import de.mscho.toftws.service.calendar.CategoryService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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
