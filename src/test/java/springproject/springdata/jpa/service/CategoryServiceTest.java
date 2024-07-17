package springproject.springdata.jpa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.reactive.TransactionalOperator;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    void success() {
        assertThrows(RuntimeException.class, () -> {
            categoryService.create();
        });
    }

    @Test
    void failled() {
        assertThrows(RuntimeException.class, ()-> {
            categoryService.test();
        });
    }

    @Test
    void pragmaticTransaction() {
        assertThrows(RuntimeException.class, ()->{
            categoryService.createWithTransactionalOpr();
        });

    }
}