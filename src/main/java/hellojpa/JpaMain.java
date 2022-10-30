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

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * proxy
             * EntityManager.getReference()
             * getReference()로 조회한 Entity를 getClass()로 출력해보면 HibernateProxy가 뜨는 것을 볼 수 있음
             * 실제 Entity와 껍데기는 똑같은데 안에가 비어 있다.
             * 내부에는 target이라는게 있는데 이것이 진짜 Reference를 가르친다 (초기에는 null 값)
             * 실제 Entity를 상속받기 때문에 실제와 껍데기가 똑같음
             * 프록시의 특징은 PDF-프록시
             */

            /**
             *
             */

            Team team = new Team();
            team.setName("teamA");

            Member member1 = new Member();
            member1.setName("memberA");
            member1.setTeam(team);

            Member member2 = new Member();
            member2.setName("memberB");
            member2.setTeam(team);

            em.persist(team);
            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            Member findMember = em.getReference(Member.class, member1.getId());
            Member findMember2 = em.find(Member.class, member2.getId());
            System.out.println("id = " + findMember.getId()); // 1차 캐시에 담겨져 있는 값이기 떄문에 DB에서 조회를 안함

            System.out.println("name = " + findMember.getName());
            // 값이 없네? 하면서 영속성 컨텍스트에 값을 요청(실제 DB조회)
            // DB로 조회한 Member Entity를 proxy객체 Member target이랑 변수와 연결해서 name값을 가져옴 (Entity에 정보가 들어가 있는 시점)

            System.out.println("name = " + findMember.getName()); // 한번더 호출하게 될경우 Entity에 값이 있기 때문에 영속성 컨텍스트에 초기화 요청을 안함
            System.out.println("getClass = " + findMember.getClass()); // Proxy 객체

            System.out.println("type 비교 = " + (findMember.getClass() == findMember2.getClass()));
            System.out.println("type 비교 = " + (findMember instanceof Member)); // JPA에서는 type 비교를 InstanceOf로 비교해주자
            System.out.println("type 비교 = " + (findMember2 instanceof Member)); // JPA에서는 type 비교를 InstanceOf로 비교해주자

            Team findTeam = em.find(Team.class, team.getId()); // Entity 조회
            Team reference = em.getReference(Team.class, team.getId());

            /**
             * EntityManager.getReference()로 조회할 떄 이미 영속성 컨텍스트 안에 있으면 proxy로 반환해주는게 아니라 Entity로 반환을 해준다(그래서 ==를 true로 반환해줌)
             * JPA 에서는 한 transaction 안에서 같은 PK로 == 을 비교하면 항상 true가 나온다
             * 
             * 근데 먼저 getReference()를 호출하고 그 다음 find()를 호출하게 되면 어떻게 될까?
             * em.find()를 호출한 결과값을 proxy로 반환한다 왜냐 ==를 비교했을 떄 항상 true를 반환해야하기 때문
             * member1 = em.getReference(Member.class, member.getId()) == proxy
             * member2 = em.find(Member.class, member.getId()) == proxy
             * (member1 == member2) == true
             */
            System.out.println("한번 조회된 reference = " + reference.getClass());
            System.out.println("a == a: " + (findTeam.getClass() == reference.getClass()));

            /**
             * 같은 타입을 getReference()로 조회했을 떄 같은 proxy로 반환된다
             * why ? O1 == O2는 true를 반환을 해줘야 하기 때문
             */

            Order order = new Order();
            order.setName("orderA");

            em.persist(order);

            Order O1 = em.getReference(Order.class, order.getId());
            Order O2 = em.getReference(Order.class, order.getId());

            System.out.println("O1 = " + O1.getClass());
            System.out.println("O2 = " + O2.getClass());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
