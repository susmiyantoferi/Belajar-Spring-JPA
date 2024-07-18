package springproject.springdata.jpa.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springproject.springdata.jpa.entity.Category;
import springproject.springdata.jpa.entity.Product;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Stream<Product> streamAllByCategory(Category category);

    //query anotation modifying digunankan untuk operasi transacsional
    @Modifying
    @Query("delete from Product p where p.name = :name")
    int deleteProductUsingName(@Param("name") String name);

    @Modifying
    @Query("update Product p set p.price = 0 where p.id = :id")
    int updateProductPriceToZero(@Param("id") Long id);

    List<Product> searchProductUsingName(@Param("name") String name, Pageable pageable);

    //query anotation
    @Query(
            value = "select p from Product p where p.name like :name or p.category.name like :name",
            countQuery = "select count(p) from Product p where p.name like :name or p.category.name like :name"
    )
    Page<Product>searchProduct(@Param("name")String name, Pageable pageable);
    @Transactional
    int deleteByName(String name);
    boolean existsByName(String name);
    Long countByCategory_Name(String name);
    List<Product> findAllByCategory_Name(String name);
    List<Product> findAllByCategory_Name(String name, Sort sort);
    //List<Product> findAllByCategory_Name(String name, Pageable pageable);
    Page<Product> findAllByCategory_Name(String name, Pageable pageable);

}
