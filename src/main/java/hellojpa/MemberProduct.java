package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class MemberProduct {

    /**
     * N : M Mapping 극복 테이블
     * 중간 테이블을 Entity로 승격 !!
     * Member와 Product랑 PK, FK로 묶어서 관리 할 수 있지만
     * 해당 테이블의 PK값은 의미없는 값으로 하는게 나중에 유연성 있게 사용할 수 있다
     */

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int count;

    private int price;

    private LocalDateTime orderDateTime;

}
