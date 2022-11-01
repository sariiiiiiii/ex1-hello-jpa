package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SubQueryMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * 서브 쿼리
             */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            /**
             * 나이가 평균보다 많은 회원
             */
            String query = "select m from Member m where m.age > (select avg(m2.age) from Member m2";

            /**
             * 한 건이라도 주문한 고객
             */
            String query2 = "select m from Member m where (select count(o) from Order o where m = o.member > 0)";

            /**
             * 서브 쿼리 지원 함수
             * EXISTS : 서브쿼리에 결과가 존재하면 참
             * ALL : 모두 만족하면 참
             * ANY, SOME : 같은 의미, 조건을 하나라도 만족하면 참
             * IN : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
             */

            /**
             * 팀 A 소속인 회원
             */
            String query3 = "select m from Member m where exists (select t from m.team t where t.name = '팀A'";

            /**
             * 전체 상품 각각의 재고보다 주문량이 많은 주문들
             */
            String query4 = "select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)";

            /**
             * 어떤 팀이든 팀에 소속된 회원
             */
            String query5 = "select m from Member m where m.team = ANY (select t from Team t)";

            /**
             * JPA 서브 쿼리 한계
             * JPA는 WHERE, HAVING절에서만 서브 쿼리 사용 가능
             * SELECT 절도 가능(하이버네이트에서 지원)
             * FROM절의 서브 쿼리는 현재 JPQL에서 불가능
             *  ㄴ 조인으로 풀 수 있으면 풀어서 해결
             *  ㄴ 아니면 query를 2번 날리거나, 애플리케이션에서 조절
             */

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
