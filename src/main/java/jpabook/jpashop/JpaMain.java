package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        /*Persistence.createEntityManagerFactory("hello");*/

        /* persistence.xml에 적어놓았던 unit-name */
        /* DB당 하나만 생성 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        /* EntityManager는 기능 당 계속 생성 */
        /* 여러 쓰레드에서 같이 쓰면 오류가 일어난다 */
        /* 한번 쓰고 버려줘야 한다 */
        EntityManager em = emf.createEntityManager();

        /* JPA에서는 트랜잭션이 정말 중요하다 */
        EntityTransaction et = em.getTransaction();
        et.begin();

        try {



            /* UPDATE */
            /* 트랜잭션을 COMMIT하는 시점에 변경된 데이터가 있으면 UPDATE 쿼리를 */
            /* 보낸 후 COMMIT 실행 !! (따로 UPDATE 쿼리를 안보내도 된다!!) */
            /*Member findMember = em.find(Member.class, 1L);
            findMember.setName("helloJPA"); 더티체킹 */

            /* Member객체를 대상으로 query를 한다 (Table 대상이 아닌) */
            /* JPQL은 객체를 대상으로 하는 객체QUERY */
            /* DB에 검색조건 query를 날리면 결국 DB에 종속이 되어버리기 때문에 */
            /* Entity 객체를 대상으로 query를 할 수 있는 JPQL이 제공된다 (SQL을 추상화한 객체지향 쿼리언어(JPQL)) */
            /* JPQL은 무조건 query가 실행될 때 flush가 일어난다 (자동flush = 그래서 commit이 일어나지 않았는데 조회가 된다) */

            // =====================================JPQL===========================================

//            List<Member> result = em.createQuery("select m from Member as m", Member.class).getResultList();
//            for(Member member : result){
//                System.out.println(member);
//            }
            // =====================================JPQL===========================================

            // 요런식으로 주문한 사람을 찾게 되면 객체지향적이지 못하게 때문에 해당 domain 클래스에 private Member member(찾고자 하는 모델)을 추가해주자
            // 잘못된 방식
//            Order order = em.find(Order.class, 1L);
//            Long memberId = order.getMemberId(); // 식별자가 들어가면 끊기기 때문에 객체지향적이지 못한다
//            Member member = em.find(Member.class, memberId); 

            // 올바른 객체지향 방식 
            Order order = em.find(Order.class, 1L);
            Member member = order.getMember();


            et.commit(); /* 종료 */
        } catch (Exception e) {
            et.rollback();
        }finally {
            em.close(); /* 자원 종료 */
        }
        emf.close(); /* WAS가 내려갈 때 EntityManagerFactory 종료 */
    }

}
