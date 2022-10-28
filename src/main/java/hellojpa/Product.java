package hellojpa;

import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    /**
     * N : N Mapping
     * 객체는 양방향으로 설게 할 수 있으나 DB 관점에서는 중간 table 이 생성됨
     * 편리해 보이지만 실무에서는 사용 X
     * 연결 테이블이 단순히 연결만 하고 끝나지 않음
     * 주문시간, 수량 같은 데이터가 들어올 수 있음
     * 중간 테이블이 숨겨져있기 때문에 query문도 이상하게 나감
     * N : N 은 어떻게 한계를 극복하느냐
     * @ManytoMany => @ManytoOne, @OnetoMany로 설계하고
     * 연결 테이블을 엔티티로 승격
     */

    @Id @GeneratedValue
    private Long id;

    private String name;

    // ============================= [ N : M ] ======================================

//    @ManyToMany(mappedBy = "products")
//    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    // ==============================================================================

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
}
