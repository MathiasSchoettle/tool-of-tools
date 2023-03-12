package de.mscho.toftws.service.calendar;

import de.mscho.toftws.entity.calendar.EventCategory;
import de.mscho.toftws.repository.calendar.CategoryRepo;
import de.mscho.toftws.repository.calendar.EventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final Logger logger;
    private final CategoryRepo categoryRepo;
    private final EventRepo eventRepo;

    public EventCategory getCategory(Long id) {
        if (id == null) return null;
        return categoryRepo.findById(id).orElse(null);
    }

    public void createCategory(String name, String color) {
        var category = new EventCategory();
        category.owner = null; // TODO set user
        category.name = name;
        category.color = color;
        categoryRepo.save(category);

        logger.info("Created category: {} - {} for user {}", category.id, category.name, category.owner);
    }

    public void deleteCategory(Long id) {
        var events = eventRepo.findEventsByCategoryId(id);
        events.forEach(e -> e.category = null);
        eventRepo.saveAll(events);
        categoryRepo.deleteById(id);
        logger.info("Deleted category: id:{} - removed category from events:{}", id, events.stream().map(e -> e.id).toList());
    }
}
