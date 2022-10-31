package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * JPQL 문법
             * select m from Member as m where m.age > 18 (Member는 table이 아닌 Entity)
             * 엔티티와 속성은 대소문자 구분 O (Member, age)
             * JPQL 키워드는 대소문자 구분 X (SELECT, from, where)
             * 엔티티 이름 사용, 테이블 이름이 아님(Member)
             * 별칭은 필수(m) (as는 생략가능)
             */

            /**
             * typeQuery : 반환 타입이 명확할 때 사용
             * Query : 반환 타입이 명확하지 않을 떄 사용
             */

            /**
             * 결과 조회 API
             * query.getResultList() : 결과가 하나일 때, 리스트 반환
             *  ㄴ 결과가 없으면 빈 리스트 반환(nullPointException을 걱정하지 않아도 된다)
             * query.getSingleResult() : 결과가 정확히 하나, 단일 객체 반환
             *  ㄴ 결과가 없으면 : NoResultException
             *  ㄴ 결과가 둘 이상이면 : NonUniqueResultException
             * Spring Data JPA에서는 값이 없을 시 null, Optional을 반환해줌 헷갈리면 안됨
             */

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m from Member as m", Member.class);
            TypedQuery<String> query1 = em.createQuery("select m.username from Member as m", String.class);
            Query query2 = em.createQuery("select m.username, m.age from Member as m"); // 반환타입이 명확하지 않을 때

            Member singleResult = query.getSingleResult(); // 반환 값이 하나 일때
            List<Member> resultList = query.getResultList(); // Collection이 반환 될때는 getResultList()

            /**
             * 파라미터 바인딩
             * 위치기준, 이름기준
             * 왠만하면 순서를 명확히 해줘야 되는 위치기준 말고 이름기준으로 파라미터를 바인딩 해주자 !!
             */
            // 위치 기준
            Member result = em.createQuery("select m from Member as m where m.username = ?1", Member.class)
                    .setParameter(1, "memberA")
                    .getSingleResult();

            // 이름 기준
            Member result2 = em.createQuery("select m from Member as m where m.username = :username", Member.class)
                    .setParameter("username", "memberA")
                    .getSingleResult();
            System.out.println("singleResultMember = " + result.getUsername());

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
