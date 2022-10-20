package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    /**
     * 회원같은 경우 회원은 회원에서 끊어야 한다
     * 회원이 주문한 목록을 보고 싶으면 Order의 외래키 member_id로 조회를 하지 member에서 List<Order> 로 보통 조회하지 않기 때문
     */

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    private String city;

    private String street;

    private String zipcode;

    @OneToMany(mappedBy = "member") // 연관관계의 주인은 외래키로 걸려있는 Order Entity에 Member이기 때문에 mappedBy = member
    private List<Order> orders = new ArrayList<>(); // JPA hibernate 관례상 리스트 초기화

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
