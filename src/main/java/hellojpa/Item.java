package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Item extends BaseEntity {
    // Item에 BaseEntity를 상속받음으로서 Item클래스를 상속받는 Album, Book, Movie 클래스에서는 BaseEntity를 상속받을 필요가 없음 !

    /**
     * ORDER_ITEM 입장에서는 어떤 상품이 들어왔는지 중요한데
     * ITEM 입장에서는 어떤 주문에 의해서 상품됐는지가 연관관계를 찾을정도까지는 중요하지 않기 때문에 ITEM에서는 연관관계 매핑 X
     */

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
