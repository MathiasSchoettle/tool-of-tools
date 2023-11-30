package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.payload.EventCategoryPayload;
import de.mscho.toftws.calendar.service.CategoryService;
import de.mscho.toftws.configuration.security.AuthenticatedUserProvider;
import de.mscho.toftws.response.ApiResponse;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping
    public ResponseEntity<ApiResponse> getCategories() {
        var categories = categoryService.getCategories(authenticatedUserProvider.getAuthenticatedUser());
        var payload = categories.stream().map(EventCategoryPayload::fromEntity).toList();

        return new ResponseEntity<>(ApiResponse.success(payload), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@RequestParam String name, @RequestParam @Pattern(regexp = "#\\d{6}") String color) {
        categoryService.createCategory(name, color);

        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam long id) {
        categoryService.deleteCategory(id);

        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }
}
