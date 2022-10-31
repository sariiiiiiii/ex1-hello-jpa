package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    /**
     * 임베디드의 장점
     * 1. 재사용
     * 2. 높은 응집성
     * 3. Period.isWork()처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음
     * 4. 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함
     */

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String name;

    // Period(기간)
    @Embedded
    private Period workPeriod;

    // 임베디드 타입이 null이면 매핑한 컬럼은 모두 null
//    @Embedded
//    private Period workPeriod = null;

    // address (주소)
    @Embedded
    private Address homeAddress;

    // @AttributeOverrides = 속성 재정의
    // 한 엔티티에서 같은 값 타입을 사용하려면 @AttributeOverrides 사용
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city",
                    column = @Column(name = "WORK_CIRY")),
            @AttributeOverride(name = "street",
                    column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode",
                    column = @Column(name = "WORK_ZIPCODE"))})
    private Address workAddress;

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

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }
}
