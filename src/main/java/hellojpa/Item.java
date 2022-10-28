package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {

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

}
