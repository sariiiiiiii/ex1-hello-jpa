package hellojpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

// 값을 정의하는 곳에 표시
@Embeddable
public class Address {

    private String city;

    private String street;

    @Column(name = "ZIPCODE") // annotaion 가능
    private String zipcode;

    // equals() @override(재정의)
    // 참고로 equals를 구현하면 거기에 맞게 hashCode도 구현해야 해줘야함, hash를 사용하는 자바 컬렉션에서 효율적으로 사용할 수 있기 때문
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }

    // 기본 생성자는 만들어줘야 됨
    public Address() {
    }

    public Address(String city) {
        this.city = city;
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void getCity(String city) {
        this.city = city;
    }
}
