package springproject.springdata.jpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springproject.springdata.jpa.entity.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    

    @Test
    void insertTest() {
        Category category = new Category();
        category.setName("Gadget");

        categoryRepository.save(category);

        Assertions.assertNotNull(category.getId());
    }

    @Test
    void updateCategory() {

        Category category = categoryRepository.findById(2L).orElse(null);
        assertNotNull(category);

        category.setName("Gadget update");
        categoryRepository.save(category);

        category = categoryRepository.findById(2L).orElse(null);
        assertNotNull(category);
        assertEquals("Gadget update", category.getName());
    }

    @Test
    void deleteCategory() {
        Category category = categoryRepository.findById(5L).orElse(null);
        assertNotNull(category);

        categoryRepository.delete(category);
    }

    @Test
    void queryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("Gadget mahal").orElse(null);
        assertNotNull(category);
        assertEquals("Gadget mahal", category.getName());

        List<Category> allByNameLike = categoryRepository.findAllByNameLike("%Gadget%");
        assertEquals(1, allByNameLike.size());
        assertEquals("Gadget mahal", allByNameLike.get(0).getName());

    }

    @Test
    void auditDate() {
        Category category = new Category();
        category.setName("simple test category");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedDate());
        assertNotNull(category.getLastModifiedDate());
    }
}