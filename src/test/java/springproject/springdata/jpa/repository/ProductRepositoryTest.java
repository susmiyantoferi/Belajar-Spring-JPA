package springproject.springdata.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.support.TransactionOperations;
import springproject.springdata.jpa.entity.Category;
import springproject.springdata.jpa.entity.Product;
import springproject.springdata.jpa.model.ProductPrice;
import springproject.springdata.jpa.model.SimpleProduct;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionOperations transactionOperations;

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
        assertEquals(2, products.size());
        assertEquals("Iphonr 16 promax", products.get(0).getName());
        assertEquals("Iphonr 15 promax", products.get(1).getName());
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

    @Test
    void testCount() {
        Long count = productRepository.count();
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("Gadget mahal");
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("Kosong");
        assertEquals(0L, count);

    }

    @Test
    void testExist() {
        boolean exists = productRepository.existsByName("Iphonr 15 promax");
        assertTrue(exists);

        exists = productRepository.existsByName("Iphonr 19 promax");
        assertFalse(exists);
    }

    @Test
    void testDeleteOld() {
        transactionOperations.executeWithoutResult(transactionStatus -> { //transactional 1
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("samsung galaxy J5");
            product.setPrice(2_000_000L);
            product.setCategory(category);
            productRepository.save(product); //transactional 1

            int delete = productRepository.deleteByName("samsung galaxy J5"); //transactional 1
            assertEquals(1, delete);

            delete = productRepository.deleteByName("samsung galaxy J5"); //transactional 1
            assertEquals(0, delete);
            // otomatis rollback jika terjadi error
        });
    }

    @Test
    void testDeleteNew() {

        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("samsung galaxy J5");
        product.setPrice(2_000_000L);
        product.setCategory(category);
        productRepository.save(product); //transactional 1 sendiri

        int delete = productRepository.deleteByName("samsung galaxy J5"); //transactional 2 sensdiri
        assertEquals(1, delete);

        delete = productRepository.deleteByName("samsung galaxy J5"); //transactional 3 sendiri
        assertEquals(0, delete);
        // tidak otomatis rollback jika terjadi error
    }

    @Test
    void namedQueryWithPageable() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Product> products = productRepository.searchProductUsingName("Iphonr 15 promax", pageable);
        assertEquals("Iphonr 15 promax", products.get(0).getName());
        assertEquals(1L, products.size());
    }

    @Test
    void searchProductLikeWithPageable() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.searchProduct("%Iphonr%", pageable);
        assertEquals(1, products.getContent().size());

        assertEquals(2, products.getTotalPages());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());

        products = productRepository.searchProduct("%Gadget%", pageable);
        assertEquals(1, products.getContent().size());

        assertEquals(2, products.getTotalPages());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
    }

    @Test
    void deleteWithModifying() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            int deleteProduct = productRepository.deleteProductUsingName("salah");
            assertEquals(0, deleteProduct);

            deleteProduct = productRepository.updateProductPriceToZero(1L);
            assertEquals(1, deleteProduct);

            Product product = productRepository.findById(1L).orElse(null);
            assertNotNull(product);
            assertEquals(0L, product.getPrice());

        });
    }

    @Test
    void stream() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Stream<Product> stream = productRepository.streamAllByCategory(category);
            stream.forEach(product -> System.out.println(product.getId() + " : " + product.getName()));
        });
    }

    @Test
    void slice() {
        Pageable pageable = PageRequest.of(0, 1);

        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Slice<Product> slice = productRepository.findAllByCategory(category, pageable);

        //manampilkan konten product
        while (slice.hasNext()) {
            slice = productRepository.findAllByCategory(category, slice.nextPageable());
            // menampilkan konten product
        }
    }

    @Test
    void specification() {
        Specification<Product> specification = (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("name"), "Iphonr 15 promax"),
                            criteriaBuilder.equal(root.get("name"), "Iphonr 16 promax")
                    )
            ).getRestriction();
        };

        List<Product> products = productRepository.findAll(specification);
        assertEquals(2, products.size());
    }

    @Test
    void projection() {
        List<SimpleProduct> simpleProducts = productRepository.findAllByNameLike("%Iphonr%", SimpleProduct.class);
        assertEquals(2, simpleProducts.size());

        List<ProductPrice> productPrices = productRepository.findAllByNameLike("%Iphonr%", ProductPrice.class);
        assertEquals(2, productPrices.size());

    }


}