package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

import static hellojpa.jpql.MemberType.ADMIN;

public class CourseExpressionMain {
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
             * 경로 표현식
             * .(점)을 찍어 객체 그래프를 탐색하는 것
             * select m.username       => 상태 필드
             *      from Member m
             *      join m.team t      => 단일 값 연관 필드
             *      join m.orders o    => 컬렉션 값 연관 필드
             * where t.name = '팀A'
             */

            /**
             * 상태 필드(state field) : 단순히 값을 저장하기 위한 필드
             *    ㄴ ex : m.username
             * 연관 필드(association field) : 연관관계를 위한 필드
             *    ㄴ 단일 값 연관 필드 :
             *       ㄴ @ManyToOne, @OneToOne, 대상이 엔티티(ex : m.team)
             *    ㄴ 컬렉션 값 연관 필드 :
             *       ㄴ @OneToMany, @ManyToMany, 대상이 컬렉션(ex : m.orders)
             */

            /**
             * 경로 표현식 특징
             * 상태 필드(state field) : 경로 탐색의 끝, 탐색 X
             * 단일 값 연관 경로 : 묵시적 내부 조인(inner join) 발생, 탐색 O
             * 컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색 X
             *    ㄴ FROM절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
             */

            /**
             * 실무에서는 묵시적 조인은 왠만하면 쓰지말고 명시적 조인을 쓰자 !!!!!
             * 상당히 난처한 상황이 올 확률이 높다
             */

            // 상태 필드 m.username에서 더이상 경로를 탐색 할 수 없다
            String query = "select m.username from Member m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList) {
                System.out.println("name = " + s);
            }

            // 단일 값 연관 경로
            // m.team에서 경로탐색을 쭉쭉 해나아갈 수 있다 (상태필드가 나타나기전까지)
            // 값 타입인 경우에도 사용할 수 있음
            // @ManyToOne, @OneToOne
            // m(Member)를 조회할 때는 join이 발생하지 않지만 연관관계가 맺어진 객체를 조회를 할 경우(fetch = FetchType.LAZY)일 때, 묵시적인 inner join 발생
            String query2 = "select m.team.name From Member m";
            List<String> resultList1 = em.createQuery(query2, String.class)
                    .getResultList();
            for (String s : resultList1) {
                System.out.println("team = " + s);
            }

            // 컬렉션값 연관경로
            // 컬렉션값 연관경로도 묵시적인 join은 들어가지만 탐색 X
            // t.members는 컬렉션이기 때문에 더이상 경로탐색을 할 수가 없음 (쓸 수 있는게 size)
            String query1 = "select t.members from Team t";
            Collection resultList2 = em.createQuery(query1, Collection.class)
                    .getResultList();

            for (Object o : resultList2) {
                System.out.println(o);
            }

            /**
             * 경로 탐색을 사용한 묵시적 조인 시 주의 사항
             * 1. 항상 내부 조인
             * 2. 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야 함
             * 3. 경로 탐색은 주로 SELECT, WHERE절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM (JOIN)절에 영향을 줌
             */

            /**
             * 실무 조언
             * 가급적 묵시적 조인 대신 명시적 조인 사용 !!!!!!!!!!!!!!!
             * 조인은 SQL 튜닝에 중요 포인트
             * 묵시적 조인이 일어나는 상황을 한눈에 파악하기 어려움
             */

            // 컬렉션 쿼리 from절에 명시적 조인
            String query3 = "select m.username from Team t join t.members m";
            List<String> resultList3 = em.createQuery(query3, String.class)
                    .getResultList();
            for (String s : resultList3) {
                System.out.println("s = " + s);
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
