package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * 양방향 매핑시 연관관계의 주인에 값을 입력해야 한다
             * 순수한 객체 관계를 고려하면 항상 양쪽다 값을 입력해야 한다.(양쪽에 다 넣어주자)
             */

            /**
             *  연관관계 편의메소드는 양쪽으로 맞춰주지 말고 한쪽에다가 설정해주자(무한루프 위험)
             *  member.setTeam(team) member에 맞춘 편의메소드
             *  team.addMember(member) team에 맞춘 편의메소드
             */

            /**
             * 라이브러리 무한루프 조심
             * lombok을 이용한 toString을 사용할 때는 주의깊게 써야함
             * Controller에서 Entity 자체를 JSON으로 반환해버리면 무한루프가 생길 수 있고, Entity를 API에 반환을 해버리면 Entity를 변경하는 순간 API의 스펙이 바뀌게 된다
             * Controller에서는 절대 Entity를 반환하지 말자 (Controller에서 DTO로 변환을 해서 반환을 해주자) !!!!!
             */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            // 저장
            Member member = new Member();
            member.setUsername("member1");
//            member.setTeam(team); // 양방향 값 입력
            em.persist(member);

            // 연관관계에 주인이기 때문에 외래키값 꺼내서 넣음, 양뱡향 값 입력
            // Member 클래스에서 setTeam할 때 자기 자신을 넣어주는 코드 입력
            team.addMember(member);// 편의메소드

            // flush와 clear를 해버리면 1차캐시에 아무것도 없기 때문에 DB에서 다시 조회
            // 영속성 콘텍스트 초기화
            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();
            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }

            // commit되는 시점에 flush도 됨
            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
