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

            // 양방향 연관관계 예제
            // Order의 orderItems list를 한 코드
            Order order = new Order();
            order.addOrderItem(new OrderItem());

            /**
             * Order에 orderItems list를 안한 코드
             * 양방향 연관관계가 아니더라도 application 개발하는데 아무 문제 없다
             * 단방향 연관관계만 해도 application 커멘더성은 다 개발할 수 있다
             * 양방향 연관관계를 만드는이유는 개발상 편의랑 조회를 위해
             * Order를 조회했을 때 OrderItem을 다 조회하고 싶어 할 때 사용(JPQL)
             * 할 수 있으면 최대한 단방향으로 하자
             */
            Order order2 = new Order();
            em.persist(order2);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order2);
            em.persist(orderItem);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
