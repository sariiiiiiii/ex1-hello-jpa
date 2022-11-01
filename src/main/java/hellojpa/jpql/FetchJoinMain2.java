package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinMain2 {
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
             * FETCH 조인의 한계
             * 페치 조인 대상에는 별도의 별칭을 줄 수 없다
             *   ㄴ 하이버네이트는 가능, 가급적 사용 X
             * 둘 이상의 컬렉션은 페치 조인 할 수 없다
             * 컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다
             *   ㄴ 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
             *   ㄴ 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
             */

            // t.members m (X)
            // 페치 조인이라는 것은 나랑 연관된 애들은 다 끌고 오는 것이다
            // JPA는 팀을 조회할 때 팀에 속해있는 member를 모두 다 가져오는 것을 원칙으로 하고 있다
            // 근데 alias를 줘서 그 member중에 3명만 뽑고 싶다면 처음부터 member를 조회해야지 team에서 접근하면 안됨
            // join fetch를 몇군데를 쓸 경우가 있는데 그 경우에만 alias를 써주고 그 외 경우에는 왠만하면 안쓴다
            // https://www.inflearn.com/course/ORM-JPA-Basic/unit/21743
            String query = "select t from Team t join fetch t.members m"; 
            List<Team> teamList = em.createQuery(query, Team.class)
                    .getResultList();

            // 일대다도 문제가 많은데 일대다대다 까지 다중 컬렉션을 조회해버리면 미쳐버린다 하지말자
            String query1 = "select t from Team t join fetch t.members join fetch t.orders";
            List<Team> teamList1 = em.createQuery(query1, Team.class)
                    .getResultList();



            // 일대다, 다대다가 페이징이 안되는 이유는 데이터가 페치조인할 떄 데이터가 뻥튀기가 되기 떄문이다
            // 그리고 앞서 말했다시피 members를 조회할 때는 모든 member가 다 나와야 하는데 몇개만 뽑아줘 이런거는 객체그래프와 맞지 않는다
            String query2 = "select m from Member m join fetch m.team"; // 다대일로 바꾸기
            List<Team> teamList2 = em.createQuery(query2, Team.class)
                    .getResultList();

            // persistence.xml 설정파일에서 설정과 class @BatchSize로 해결
            String query3 = "select t from Team t"; // 다대일로 바꾸기
            List<Team> teamList4 = em.createQuery(query3, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for (Team team : teamList4) {
                System.out.println("team = " + team.getName() + " || size = " + teamList4.size());
                for (Member m : team.getMembers()) {
                    System.out.println("m = " + m);
                }
            }

            /**
             * 페치 조인의 특징과 한계
             * 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화
             * 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
             *   ㄴ @OneToMany(fetch = FetchType.LAZY) // 글로벌 로딩 전략
             * 실무에서 글로벌 로딩 전략은 모두 지연 로딩
             * 최적화가 필요한 곳은 페치 조인 적용
             */

            /**
             * 정리
             * 모든것을 페치조인으로 해결할 수는 없음
             * 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
             * 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면, 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
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
