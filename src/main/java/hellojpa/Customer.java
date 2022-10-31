package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
public class Customer {

    /**
     * 값 타입 컬렉션
     * @ElementCollection : 값 타입 컬렉션 필드 지정
     * @CollectionTable : DB 컬렉션 테이블 name 및 FK 설정
     * 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다
     * 컬렉션을 저장하기 위한 별도의 테이블이 필요함
     */

    @Id
    @GeneratedValue
    @Column(name = "CUSTOMER_ID")
    private Long id;

    @Column(name = "CUSTOMER_NAME")
    private String name;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
        @JoinColumn(name = "CUSTOMER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name = "ADDRESS", joinColumns =
//        @JoinColumn(name = "CUSTOMER_ID")
//    )
//    private List<Address> addressHistory = new ArrayList<>();

    // 값 타입 컬렉션으로 사용하지 말고 Entity로 만들어서 일대다 단방향 관계로 매핑
    // 영속성 전이 + 고아 객체 제거 활용
    @OneToMany(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressEntity = new ArrayList<>();

    @Embedded
    private Period period;

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

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }


    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<AddressEntity> getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(List<AddressEntity> addressEntity) {
        this.addressEntity = addressEntity;
    }
}
