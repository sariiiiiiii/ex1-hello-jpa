package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.awt.*;
import java.util.List;

public class JpaMain2 {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 즉시로딩 - EAGER
             * 즉시로딩 주의
             * 1. 가급적 지연 로딩만 사용(특히 실무에서)
             * 2. 즉시 로딩을 적용하면 예상치 못한 SQL이 발생(예제로 2개 테이블만 조인되서 그렇지 많아지면 많아질수록 모든게 join 되기 때문에 성능상 이슈)
             * 3. 즉시 로딩은 JQPL에서 N + 1 문제를 일으킨다
             * 4. @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정
             * 5. @OneToMany, @ManyToMany는 기본이 지연로딩
             */

            Team2 team = new Team2();
            team.setName("teamA");
            em.persist(team);

            Member2 member = new Member2();
            member.setName("memberA");
            member.setTeam2(team);

            em.persist(member);

            em.flush();
            em.clear();
            
            // EAGER = 즉시로딩은 조회할 때 team까지 함께 조회
            Member2 m = em.find(Member2.class, member.getId());

            System.out.println("m = " + m.getTeam2().getClass()); // proxy가 아닌 Entity

            System.out.println("======================================");
            System.out.println("teamName = " + m.getTeam2().getName()); // Entity에서 값을 가져옴
            System.out.println("======================================");

            // JPQL에서의 문제점
            // JPQL은 query를 그대로 sql로 변역을 하는 것이기 때문에 그대로 번역을 하게 되면 member만 조회하게 됨
            // member를 가지고 와봤더니 Member 도메인에 team이 즉시로딩으로 되어있네 ? 하면서 member의 갯수가 10개면 10개만큼의 team 값을 다시 가져오게 됨 (query 2번 실행)
            List<Member2> result = em.createQuery("select m from Member m", Member2.class)
                            .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
