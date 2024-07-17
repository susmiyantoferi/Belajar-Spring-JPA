package springproject.springdata.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.TransactionOperations;
import springproject.springdata.jpa.entity.Category;
import springproject.springdata.jpa.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionOperations transactionOperations;


    private void error(){
        throw new RuntimeException("UPS");
    }
    public void createWithTransactionalOpr(){
        transactionOperations.executeWithoutResult(transactionStatus -> {
            for (int i=0; i<5; i++){
                Category category = new Category();
                category.setName("Category "+ i);
                categoryRepository.save(category);
            }
            error();
        });
    }

    @Transactional
    public void create(){

        for (int i = 0; i < 5; i++){
            Category category = new Category();
            category.setName("Category "+ i);
            categoryRepository.save(category);
        }

        throw new RuntimeException("ups rollback please");
    }

    public void test(){
        create();
    }
}
