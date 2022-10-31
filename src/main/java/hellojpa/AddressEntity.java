package hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    /**
     * 값 타입 컬렉션 대안
     * 1. 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
     * 2. 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
     * 3. 영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
     * 4. Ex) AddressEntity
     */

    @Id
    @GeneratedValue
    private Long id;

    private Address address;

    public AddressEntity() {
    }

    public AddressEntity(Long id, Address address) {
        this.id = id;
        this.address = address;
    }

    public AddressEntity(String city, String street, String zipcode) {
        this.address = new Address(city, street, zipcode);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
