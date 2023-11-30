package de.mscho.toftws.calendar.service;

import de.mscho.toftws.configuration.security.AuthenticatedUserProvider;
import de.mscho.toftws.calendar.entity.EventCategory;
import de.mscho.toftws.calendar.repository.CategoryRepo;
import de.mscho.toftws.calendar.repository.EventRepo;
import de.mscho.toftws.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final Logger logger;
    private final CategoryRepo categoryRepo;
    private final EventRepo eventRepo;
    private final AuthenticatedUserProvider authenticationProvider;

    public List<EventCategory> getCategories(User user) {
        return categoryRepo.findByUser(user);
    }

    public Optional<EventCategory> getCategory(Long categoryId) {
        var user = authenticationProvider.getAuthenticatedUser();
        return categoryRepo.findByIdAndUser(categoryId, user);
    }

    public void createCategory(String name, String color) {
        var user = authenticationProvider.getAuthenticatedUser();
        var category = new EventCategory();

        category.user = user;
        category.name = name;
        category.color = color;
        categoryRepo.save(category);

        logger.info("Created category: {} - {} for user {}", category.id, category.name, category.user);
    }

    public void deleteCategory(Long categoryId) {
        var user = authenticationProvider.getAuthenticatedUser();
        var events = eventRepo.findEventsByCategoryIdAndUser(categoryId, user);

        events.forEach(e -> e.category = null);
        eventRepo.saveAll(events);
        categoryRepo.deleteByIdAndUser(categoryId, user);

        logger.info("Deleted category: id:{} - removed category from events:{}", categoryId, events.stream().map(e -> e.id).toList());
    }

    public boolean categoryExists(Long categoryId) {
        var user = authenticationProvider.getAuthenticatedUser();
        return categoryRepo.existsByIdAndUser(categoryId, user);
    }
}
