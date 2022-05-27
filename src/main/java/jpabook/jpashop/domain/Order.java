package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS") // DB에는 ORDER가 예약어(order by)로 걸려 있기 때문에 클래스는 Order로 만들어 주고 @Table로 DB와 매핑시켜주자
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId; // 누가 주문을 했는지 알아야 하기 때문에

    private Member member;

    // 자바 8이후로부터는 자바에서 LocalDateTime에 대해서 처리를 해주기 때문에 따로 건드리지 않아도 된다

    private LocalDateTime orderDate; // hibernate 관례로 orderDate로 짜주면 DB에도 orderDate로 들어가는데 SpringBoot에서는 자바의 camelcase를 읽어서 언더바를 넣어줌(order_date)
    @Enumerated(EnumType.STRING) // EnumType.ORDINAL로 하면 순서로 들어가기 때문에 중간에 enum 클래스에 순서가 바뀌게 되면 엉키기 때문에 EnumType.STRING으로 해주자
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
