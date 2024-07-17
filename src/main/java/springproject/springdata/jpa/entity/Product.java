package springproject.springdata.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")

@NamedQueries(
        @NamedQuery(
                name = "Product.searchProductUsingName",
                query = "SELECT p from Product p where p.name = :name"
        )
)

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "categories_id", referencedColumnName = "id")
    private Category category;
}
