package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static hellojpa.jpql.MemberType.ADMIN;

public class ExtendsMain {
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



            /**
             * 다형성 쿼리
             */

            /**
             * 조회 대상을 특정 자식으로 한정
             * 예) Item중에 Book, Movie를 조회해라
             * [JPQL] select i from Item i where type(i) IN (BOOK, MOVIE)
             * [SQL] SELECT I FROM ITEM I WHERE I.DTYPE IN ('BOOK', 'MOVIE')
             */

            /**
             * 예) 부모인 Item과 자식인 Book이 있다 (SINGLE_TABLE 전략일 때)
             * [JPQL] select i from Item i where treat(i as Book).auther = 'kim'
             * [SQL] select i.* from item i where i.DTYPE = 'B' and i.auther = 'kim'
             */

            /**
             * 엔티티 직접 사용 - 기본 키 값
             * JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용
             * [JPQL] select count(m.id) from Member m -> 엔티티의 아이디를 사용
             * [JPQL] select count(m) from Member m -> 엔티티를 직접 사용
             * [SQL] select count(m.id) as cnt from Member m -> JPQL 둘다 같은 SQL 실행
             */

            /**
             * 엔티티를 파라미터로 전달해도 식별자를 직접 전달하는 것과 같은 SQL이 발생하게 됨
             */
            Integer singleResult = em.createQuery("select count(m) from Member m", Integer.class)
                    .getSingleResult();

            /**
             * 외래키 값 직접 사용
             * 외래키값을 넣거나 엔티티를 직접 넣어도 나가는 SQL문은 동일
             */
            List<Member> resultList = em.createQuery("select m from Member m where m.team = :Team", Member.class)
                    .setParameter("Team", team)
                    .getResultList();

            List<Member> resultList1 = em.createQuery("select m from Member m where m.team.id = :teamId", Member.class)
                    .setParameter("teamId", team.getId())
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
