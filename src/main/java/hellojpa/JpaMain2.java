package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;

public class JpaMain2 {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
             * Hibernate는 org.hibernate.LazyInitializationException 발생
             */

            Member member1 = new Member();
            member1.setName("memberA");

            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass()); // proxy

            // proxy에 초기화 요청은 영속성 컨텍스트를 통해 처리가 되는데 영속성 컨텍스트를 끊게 되면은 ? (detach(), close())
//            em.detach(refMember);

            // 영속성 컨텍스트를 끊었기 때문에 catch()문에서 Exception 발생(LazyInitializationException)
            refMember.getName();// DB query 조회

            /**
             * proxy 확인 util
             */
            // 프록시 인스턴스의 초기화 여부 확인(Entity manager Factory)
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            // 프록시 클래스 확인 방법
            System.out.println("proxy = " + refMember.getClass());

            // 프록시 강제 초기화(JPA에서는 지원안하고 hibernate에서 지원)
            Hibernate.initialize(refMember); // 강제 초기화

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();

    }
}
