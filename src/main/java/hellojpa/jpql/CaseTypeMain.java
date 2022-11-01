package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static hellojpa.jpql.MemberType.ADMIN;

public class CaseTypeMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * 조건식 (CASE 식)
             */
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
             * 기본 CASE 식
             */
            String query =
                    "select " +
                        "case when m.age <= 10 then '학생요금'" +
                        "     when m.age >= 60 then '경로요금'" +
                        "     else '일반요금' " +
                            "end " +
                    "from Member m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println(s);
            }

            /**
             * 단숙 CASE 식
             */
            String query2 =
                    "select " +
                        "case t.name" +
                        "     when '팀A' then '인센티브 110%'" +
                        "     when '팀B' then '인센티브 120%'" +
                            " else '인센티브 150%' " +
                            "end" +
                    " from Team t";
            List<String> resultList1 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s : resultList1) {
                System.out.println(s);
            }

            /**
             * COALESCE : 하나씩 조회해서 null이 아니면 반환
             * 사용자 이름이 없으면 이름 없는 회원을 반환
             */
            String query3 = "select coalesce(m.username, '이름 없는 회원') from Member m";
            List<Member> resultList2 = em.createQuery(query3, Member.class)
                    .getResultList();

            for (Member member1 : resultList2) {
                System.out.println(member1.toString());
            }

            /**
             * NULLIF : 두 값이 같으면 null 반환, 다르면 첫 번째 값 반환
             * 사용자 이름이 '관리자'면 null을 반환하고 나머지는 본인의 이름을 반환
             */
            String query4 = "select nullif(m.username, '관리자') from Member m";
            List<Member> resultList3 = em.createQuery(query4, Member.class)
                    .getResultList();
            for (Member member1 : resultList3) {
                System.out.println(member1.toString());
            }

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
