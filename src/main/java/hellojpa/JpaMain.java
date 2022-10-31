package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 임베디드 타입과 테이블 매핑
             * 1. 임베디드 타입은 엔티티의 값일 뿐이다
             * 2. 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다
             * 3. 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능
             * 4. 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음
             */

            Address address = new Address("city", "street", "zipcode");
            Period period = new Period(LocalDateTime.now(), LocalDateTime.now());

            Member memberA = new Member();
            memberA.setName("memberA");
            memberA.setHomeAddress(address);
            memberA.setWorkPeriod(period);
            em.persist(memberA);

            /**
             * 임베디드 타입같은 값 타입을 여러 엔티티에서 공유하면 위험 !
             * 부작용(Side Effect) 발생
             * 해결점은 임베디드타입을 복사해서 사용 !
             * 만약 같이 값을 공유하고 싶다, 값을 같이 바꾸고 싶다 할 경우는 address를 임베디드 타입이 아닌 Entity로 생성해서 사용해야 함
             */

            /**
             * 객체 타입의 한계
             * 1. 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다
             * 2. 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입(primitive type)이 아니라 객체 타입이다
             * 3. 자바 기본 타입에 값을 대입하면 값을 복사한다
             * 4. 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다
             * 5. 객체의 공유 참조는 피할 수 없다
             */

            /**
             * 불변 객체
             * 1. 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
             * 2. 값 타입은 불변 객체(immutable object)로 설계해야 함
             * 3. 불변 객체 : 생성 시점 이후 절대 값을 변경할 수 없는 객체
             * 4. 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨
             * 5. 참고 : Integer, String은 자바가 제공하는 대표적인 불변 객체
             */

            // 임베디드타입의 값타입 복사
            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

            Member memberB = new Member();
            memberB.setName("memberB");
            memberB.setHomeAddress(copyAddress);
            memberB.setWorkPeriod(period);
            em.persist(memberB);

            // memberA만 값을 바꾸고 싶은데 바꿔주게 될경우 update query가 2번 나가고 memberB까지 값이 바뀌가 됨
            memberA.getHomeAddress().getCity("newCity");

            /**
             * 불변 객체로 Setter까지 없앴다
             * 그런데 값을 바꾸고 싶으면 어떻게 해야 할까?
             * 값을 하나만 변경하는것이 아니라 값을 통째로 변경해주는 것이 맞다
             * 불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을 수 있다
             */

            Member memberC = new Member();
            memberC.setName("memberC");

            Address newAddress = new Address("new City", address.getStreet(), address.getZipcode());
            memberC.setHomeAddress(newAddress);
            em.persist(memberC);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
