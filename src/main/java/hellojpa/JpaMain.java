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

            Member member = new Member();
            member.setName("memberA");
            member.setHomeAddress(new Address("city", "street", "zipcode"));
            member.setWorkAddress(new Address("city2", "street2", "zipcode2"));
            member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));
            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
