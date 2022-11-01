package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class NamedQueryMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            Team team = new Team();
            em.persist(team);

            Member member = new Member();
            member.setUsername("A");
            em.persist(member);
            Member member1 = new Member();
            member.setUsername("B");
            em.persist(member1);

            em.flush();
            em.clear();

            /**
             * Named 쿼리 - 정적 쿼리
             * 미리 정의해서 이름을 부여해두고 사용하는 JPQL
             * 정적 쿼리
             * 어노테이션, XML에 정의
             * 
             * [어마어마한 이점]
             * 애플리케이션 로딩 시점에 초기화 후 재사용 (로딩 시점에 JPA가 SQL로 파싱을 한고 캐시로 가지고 있는다 어차피 정적쿼리라 문자가 변하지 않으니까)
             * 애플리케이션 로딩 시점에 쿼리를 검증 (잘못된 쿼리가 있으면 실행하는 시점에 querySyntaxException이 발생한다)
             * Spring Data JPA를 사용하게 되면 @query라는 어노테이션을 사용할텐데 이 어노테이션이 JPA의 namedQuery를 구현한 것이다
             */

            /**
             * Named 쿼리 환경에 따른 설정
             * XML이 항상 우선권을 가진다
             * 애플리케이션 운영 환경에 따라 다른 XML을 배포할 수 있다
             */

            List<Member> memberList = em.createNamedQuery("Member.findbyUsername", Member.class)
                    .setParameter("username", member.getUsername())
                    .getResultList();

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
