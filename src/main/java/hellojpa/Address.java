package hellojpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// 값을 정의하는 곳에 표시
@Embeddable
public class Address {

    private String city;

    private String street;

    @Column(name = "ZIPCODE") // annotaion 가능
    private String zipcode;

    // 기본 생성자는 만들어줘야 됨
    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
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
}
