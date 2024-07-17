package springproject.springdata.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springproject.springdata.jpa.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    int deleteByName(String name);
    boolean existsByName(String name);
    Long countByCategory_Name(String name);
    List<Product> findAllByCategory_Name(String name);
    List<Product> findAllByCategory_Name(String name, Sort sort);
    //List<Product> findAllByCategory_Name(String name, Pageable pageable);
    Page<Product> findAllByCategory_Name(String name, Pageable pageable);

}
