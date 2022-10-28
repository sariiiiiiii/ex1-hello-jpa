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

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
