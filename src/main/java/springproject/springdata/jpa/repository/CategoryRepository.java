package springproject.springdata.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.springdata.jpa.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // select where name = ?
    Optional<Category> findFirstByNameEquals(String name);

    // select where name like = ?
    List<Category> findAllByNameLike(String name);

}
