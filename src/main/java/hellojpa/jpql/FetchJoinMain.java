package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static hellojpa.jpql.MemberType.ADMIN;

public class FetchJoinMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * FETCH JOIN 실무에서 정말정말 너무너무 중요함 !!
             */

            /**
             * SQL 조인 종류 X (SQL 명령어가 아니라 JPQL에서 성능 최적화를 위해 만듬)
             * JPQL에서 성능 최적화를 위해 제공하는 기능
             * 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 가능
             * join fetch 명령어 사용
             * 페치 조인 :: = [LEFT [OUTER] || INNER] JOIN FETCH 조인경로
             */

            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Team team3 = new Team();
            team3.setName("팀C");
            em.persist(team3);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("회원4");
            em.persist(member4);

            em.flush();
            em.clear();

            /**
             * 엔티티 페치 조인
             * 회원을 조회하면서 연관된 팀도 함께 조회 (SQL 한 번에)
             * SQL을 보면 회원 뿐만 아니라 팀(T.*)도 함께 SELECT
             *
             * [JPQL] select m from Member m join fetch m.team (분명 조회 alias는 m(Member)만 있지만 SQL을 보면 TEAM도 조회)
             * [SQL] SELECT M.*, T.* FROM MEMBER INNER JOIN TEAM M.TEAM.ID = T.ID (조회는 T.*도 같이 됨)
             */

            String query = "select m from Member m";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();

            // 회원1, 팀A (SQL)
            // 회원2, 팀A (팀 A는 위에서 팀A 조회한 결과에 1차캐시에 저장되어 있기 때문에 SQL X)
            // 회원3, 팀B (팀 B의 대한 정보는 1차캐시에 없기 때문에 SQL)
            // 결국 팀의 대한 정보 DB조회는 2번
            // 이 때의 팀의 대한 정보는 proxy 객체
            for (Member member : resultList) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }


            String query1 = "select m from Member m join fetch m.team";
            List<Member> resultList1 = em.createQuery(query1, Member.class)
                    .getResultList();

            // Member와 Team이 한번에 조회되기 때문에 proxy 객체가 아님
            // SQL은 1번만 실행
            // FetchType.LAZY 지연로딩으로 셋팅을 했어도 fetch가 우선이기 때문에 함께 조회
            for (Member member : resultList1) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }

            /**
             * 컬렉션 페치 조인
             * [JPQL] select t from Team t join fetch t.members where t.name = '팀A'
             * [SQL] SELECT T.*, M.* FROM TEAM T INNER JOIN MEMBER M ON T.ID = M.TEAM.ID
             */
            String query3 = "select t from Team t join fetch t.members";
            List<Team> teamList = em.createQuery(query3, Team.class)
                    .getResultList();
            
            // DB에서 1:N 조회를 하게 되면 데이터가 뻥튀기가 된다
            // 팀A, size = 2
            // 팀A, size = 2
            // 팀B, size = 1
            // 말로 설명 힘듬 https://www.inflearn.com/course/ORM-JPA-Basic/unit/21742 참고
            for (Team team : teamList) {
                System.out.println("team = " + team.getName() + ", " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
            }

            /**
             * 이렇게 컬렉션 페치 조인을 하게 되는 경우 중복결과가 나온다
             * 그럴때는 DISTINCT를 사용하자
             * SQL의 DISTINCT는 중복된 결과는 제거하는 명령
             * JPQL의 DISTINCT는 2가지의 기능 제공
             *   ㄴ 1. SQL에 DISTINCT를 추가
             *   ㄴ 2. 애플리케이션에서 엔티티 중복 제거 (DB에서 제거하는것이 아니라 DB에서 정보가 날라오면 애플리케이션에서 중복된 엔티티 제거)
             *     ㄴ DB에서의 DISTINCT는 완전히 동일한 정보만은 제거해주기 때문에 우리가 원하는 DISTINCT는 아니다 그래서 애플리케이션에서 같은 식별자를 가진 엔티티를 중복제거 한다
             */
            String query4 = "select distinct t from Team t join fetch t.members"; // Team t에 대해서 distinct를 하겠다
            List<Team> teamList1 = em.createQuery(query4, Team.class)
                    .getResultList();

            /**
             * 페치 조인과 일반 조인의 차이
             * 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
             * [JPQL] select t from Team t join t.members m where t.name = '팀A'
             * [SQL] SELECT T.* FROM TEAM T INNER JOIN MEMBER M ON T.ID = M.TEAM.ID WHERE T.NAME = '팀A'
             * [일반조인]
             * JPQL은 결과를 반환할 떄 연관관계 고려 X
             * 단지 SELECT절에 지정한 엔티티만 조회할 뿐
             * 여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회 X
             * 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시 로딩)
             * 페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념
             */
            String query5 = "select t from Team t join t.members"; // 일반조인은 inner join문을 날리긴하는데 가져오는 정보를 Team의 정보만 가져온다(데이터 뻥튀기는 됨)
            List<Team> teamList2 = em.createQuery(query5, Team.class)
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
