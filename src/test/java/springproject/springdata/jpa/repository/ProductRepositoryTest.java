package springproject.springdata.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springproject.springdata.jpa.entity.Category;
import springproject.springdata.jpa.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    void createProduct() {
        Category category = categoryRepository.findById(3L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("Iphonr 15 promax");
            product.setPrice(12_000_000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        {
            Product product = new Product();
            product.setName("Iphonr 16 promax");
            product.setPrice(20_000_000L);
            product.setCategory(category);
            productRepository.save(product);
        }
        
    }

    @Test
    void findByCategoryName() {

        List<Product> products = productRepository.findAllByCategory_Name("Gadget mahal");
        assertEquals(2, products.size());
        assertEquals("Iphonr 15 promax", products.get(0).getName());
        assertEquals("Iphonr 16 promax", products.get(1).getName());
    }

    @Test
    void sortTest() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("Gadget mahal", sort);
        assertNotNull(products);
        assertEquals(2,products.size());
        assertEquals("Iphonr 16 promax", products.get(0).getName() );
        assertEquals("Iphonr 15 promax", products.get(1).getName() );
    }

//    @Test
//    void testPagging() {
//        //page 0
//        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
//        List<Product> products = productRepository.findAllByCategory_Name("Gadget mahal", pageRequest);
//
//        //expected keluar data = 1
//        assertEquals(1, products.size());
//        //expected isi datanya, index data ke 0
//        assertEquals("Iphonr 16 promax", products.get(0).getName());
//
//
//        //page 1
//        pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
//        products = productRepository.findAllByCategory_Name("Gadget mahal", pageRequest);
//
//        assertEquals(1, products.size());
//        assertEquals("Iphonr 15 promax", products.get(0).getName());
//
//
//    }

    @Test
    void testPageResult() {
        //page 0
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.findAllByCategory_Name("Gadget mahal", pageRequest);

        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Iphonr 16 promax", products.getContent().get(0).getName());


        //page 1
        pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("Gadget mahal", pageRequest);

        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Iphonr 15 promax", products.getContent().get(0).getName());



    }
}