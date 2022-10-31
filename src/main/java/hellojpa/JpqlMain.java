package hellojpa;

import hellojpa.jpql.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            Member member = new Member();
            member.setUsername("memberA");
            em.persist(member);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            em.persist(memberB);

            // flush() -> commit(), query 날라갈 때 flush가 실행됨
            // 복잡한 query로 인해 JDBC TEMPLATE를 이용해야 할 경우가 있다면 아직 영속성 컨텍스트에서 DB로 query가 안날라긴 시점이기 때문에 수동으로 flush()를 해줘야 함

            /**
             * JPQL
             * EntityManager.find()
             * 객체 그래프 탐색(a.getB().getC())
             */

            /**
             * JPA를 사용하면 엔티티 객체를 중심으로 개발
             * 문제는 검색쿼리 !
             * 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
             * 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
             * 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요 (ex) where, group by)
             */

            /**
             * JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
             * SQL과 문법 유사, SELECT, CREATE, UPDATE, DELETE, WHERE, GROUP BY, HAVING, JOIN 지원
             * JPQL은 엔티티 객체를 대상으로 쿼리, SQL은 데이터베이스 테이블을 대상으로 쿼리 (가장 큰 차이 !!)
             */

            /**
             * 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
             * SQL을 추상화해서 특정 데이터베이스 SQL에 의존 X
             * JPQL을 한마디로 정의하면 객체 지향 SQL
             */

            // 여기에서의 Member는 Table을 가르키는데 아니라 Entity를 가르키는 것
            // query문 나간것을 보면 주석으로 JPQL이 나간것을 볼 수 있고, 실제 SQL이 나간것을 볼 수 있다
            // m = alias Member 그 자체를 의미
            List<Member> result = em.createQuery(
                    "select m from Member m where m.username like '%kim%'",
                    Member.class
            ).getResultList();

            /**
             * criteria
             * 장점 : 자바 코드로 query문을 작성하는 것이기 때문에 잘못된 query문은 컴파일시 오류잡아줌
             *       동적 쿼리를 짜기 좋음
             * 단점 : 내가 SQL을 쓰고 있는 것인지 ... 뭔지 .... 혼란스러움 ...
             */
            // criteria 사용 준비 (자바 표준 문법에서 제공하는 기능)
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            String username = "abcd";

            Root<Member> m = query.from(Member.class);
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            CriteriaQuery<Member> cq = query.select(m);

            // 동적쿼리 작성
            if (username != null) {
                cq = cq.where(cb.equal(m.get("username"), "kim"));
            }

            List<Member> resultList = em.createQuery(cq)
                            .getResultList();

            /**
             * queryDSL
             * 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
             * JPQL 빌더 역할
             * 컴파일 시점에 문법 오류를 찾을 수 있음
             * 동적쿼리 작성 편리함
             * 단순하고 쉬움
             * 실무 사용 권장 !!
             */

            // queryDSL 강의해서 활용

            /**
             * native SQL
             */
            String sql = "SELECT MEMBER_ID, USERNAME FROM MEMBER WHERE USERNAME = 'kim'";
            List<Member> nativeMember = em.createNativeQuery(sql, Member.class)
                            .getResultList();

            /**
             * JDBC 직접 사용, SpringJdbcTemplate 등
             * JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, 마이바티스등을 함께 사용 가능
             * 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요
             * 예) JPA를 우회해서 Sql을 실행하기 직전에 영속성 컨텍스트 수동 플러시
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
