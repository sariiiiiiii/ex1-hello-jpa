package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static hellojpa.jpql.MemberType.ADMIN;

public class JpqlFunctionMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            Team team = new Team();
            team.setName("팀A");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.changeTeam(team);
            member.setAge(10);
            member.setType(ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

            /**
             * JPQL 기본 함수
             * CONCAT
             * SUBSTRING
             * TRIM
             * LOWER, UPPER
             * LENGTH
             * LOCATE
             * ABS, SQRT, MOD
             * SIZE, INDEX(JPA 용도)
             * jpql이 제공하는 기본 함수이기 때문에 DB에 관계없이 쓰면 됨
             */

            String query = "select concat('a', 'b') from Member m"; // concat('a', 'b') 표준 문법, 'a' || 'b' 하이버네이트 지원문법
            String query2 = "select substring(m.username, 2, 3) From Member m"; // substring
            String query3 = "select locate('de', 'abcdefg') from Member m"; // locate
            String query4 = "select size(t.members) From Team t"; // JPA 용도 size()
            String query5 = "";

            List<Integer> resultList = em.createQuery(query4, Integer.class)
                    .getResultList();
            for (Integer s : resultList) {
                System.out.println("s = " + s);
            }

            /**
             * 사용자 정의 함수 호출
             * 하이버네이트는 사용전 방언에 추가해야 한다
             * 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다
             * select function('group_concat', i.name) from Item i
             * 하지만 다행히도 MySqlDialect를 가보면 registerFunction을 보면 다양한 함수를 제공하고 있다
             */

            // MyH2Dialect 클래스를 만들어 사용자 함수를 만들고 난 후 persistence.xml(설정파일)에서 내가 등록한 함수의 클래스를 등록을 해주어야 한다 persistence.xml 참고
            String query6 = "select function('group_concat', m.username) from Member m"; // 사용자 정의 함수 사용
            String query7 = "select group_concat(m.username) from Member m"; // function 생략 가능

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
