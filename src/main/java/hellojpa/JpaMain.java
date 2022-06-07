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

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team); // jpa가 알아서 team에서 pk값을 꺼내서 fk값에 의해서 값을 넣어준다
            em.persist(member);

            em.flush(); // 영속성 콘텍스트 초기화
            em.clear();

            Member findMember = em.find(Member.class, member.getId());

            List<Member> findMembers = findMember.getTeam().getMembers();

            for(Member m : findMembers){
                System.out.println("memberName = " + m.getUsername());
            }

            System.out.println(" 이것만 쪼리고 딱 2판 때리고 자야겠다 ㅋ ");

            et.commit();

        } catch (Exception e) {
            et.rollback();
        }finally{
            em.close();
        }
        emf.close();
    }
}
