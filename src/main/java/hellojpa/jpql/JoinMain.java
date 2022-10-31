package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JoinMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * JOIN
             * 내부 조인 : SELECT m FROM Member m [INNER] JOIN m.team t
             * 외부 조인 : SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
             * 세타 조인(연관관계가 없는) : select count(m) from Member m, Team t where m.username = t.name
             */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            // inner join (inner는 생략 가능)
            String innerQuery = "select m from Member m join m.team t where t.name = :teamName";
            List<Member> resultList = em.createQuery(innerQuery, Member.class)
                    .setParameter("teamName", "teamA")
                    .getResultList();

            // outer join(outer는 생략 가능)
            String outerQuery = "select m from Member m left join m.team t";
            List<Member> resultList1 = em.createQuery(outerQuery, Member.class)
                    .getResultList();

            System.out.println("result1 = " + resultList1.size());

            // 세타 조인 (cross join, 막 조인)
            String setaQuery = "select m from Member m, Team t where m.username = t.name";
            List<Member> resultList2 = em.createQuery(setaQuery, Member.class)
                    .getResultList();

            System.out.println("result = " + resultList2.size());

            /**
             * 조인 - ON절
             * 1. 조인 대상 필터링
             * 2. 연관관계 없는 엔티티 외부 조인
             */

            /**
             *
             * 예) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
             */
            String jpql = "select m, t from Member m left join m.team t on t.name = 'A'";
            List<Member> resultList3 = em.createQuery(jpql, Member.class)
                    .getResultList();

            /**
             * 연관관계 없는 엔티티 외부 조인
             * 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
             */
            String jpql2 = "select m, t from Member m left join Team t ON m.username = t.name";
            List<Member> resultList4 = em.createQuery(jpql2, Member.class)
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
