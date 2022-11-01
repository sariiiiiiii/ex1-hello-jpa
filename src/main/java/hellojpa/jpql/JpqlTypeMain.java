package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static hellojpa.jpql.MemberType.*;

public class JpqlTypeMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * JPQL 타입 표현
             * 문자 : 'HELLO', 'She''s'
             * 숫자 : 10L(Long), 10D(Double), 10F(Float)
             * Boolean : TRUE, FALSE
             * ENUM : jpabook.MemberType.Admin(패키지명 포함)
             * 엔티티 타입 : TYPE(m) = Member (상속관계에서 사용)
             */

            /**
             * JPQL 기타
             * SQL과 문법이 같은 식
             * EXISTS, IN
             * AND, OR, NOT
             * =, >=, >, <, <=, <>
             * BETWEEN, LIKE, IS NULL
             */

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.setType(ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m.username, 'HELLO', true from Member m where m.type = hellojpa.jpql.MemberType.ADMIN";
            String query2 = "select m.username, 'HELLO', true from Member m where m.type = :userType and m.username is not null and m.age between 0 and 10";

            List<Object[]> resultList = em.createQuery(query)
                    .getResultList();

            List<Object[]> resultList2 = em.createQuery(query2)
                    .setParameter("userType", ADMIN)
                    .getResultList();

            for (Object[] o : resultList) {
                System.out.println(o[0]);
                System.out.println(o[1]);
                System.out.println(o[2]);
                System.out.println(o[3]);
            }

            for (Object[] o : resultList2) {
                System.out.println(o[0]);
                System.out.println(o[1]);
                System.out.println(o[2]);
                System.out.println(o[3]);
            }

            /**
             * 상속관계 매핑
             * Book이라는 클래스가 Item이라는 클래스에게 상속받고 있을 때 Book에 관한 정보만 뽑고 싶을 때
             * query문이 나간것을 보면 DTYPE = 'BOOK' 이라는 query문이 나가게 됨
             */
            String query3 = "select i from Item i where type(i) = Book";
//            em.createQuery(query3, Member.class)
//                            .getResultList();

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
