package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 지연로딩 LAZY
             */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("memberA");
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());

            // 멤버 도메인에서 (fetch = FetchType = LAZY)로 설정을 해주었기 때문에 필요시에만 조회(지연로딩) 프록시객체로 반환받음
            System.out.println("m = " + m.getTeam().getClass()); // proxy

            System.out.println("======================================");
            // proxy객체가 초기화 되면서 DB에서 값을 조회(츠록시 객체 초기화 => 영속성 컨텍스트 DB 조회)
            System.out.println("team = " + m.getTeam().getName());
            System.out.println("======================================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
