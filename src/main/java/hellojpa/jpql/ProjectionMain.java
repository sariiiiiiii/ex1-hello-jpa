package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class ProjectionMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            /**
             * 프로젝션
             * SELECT 절에 조회할 대상을 지정하는 것
             * 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
             * select m from Member m -> 엔티티 프로젝션
             * select m.team from Member m -> 엔티티 프로젝션
             * select m.address from Member m -> 임베디드 타입 프로젝션
             * select m.username, m.age from Member m -> 스칼라 타입 프로젝션
             * DISTINCT로 중복 제거
             */

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            em.persist(member);
            
            em.flush();
            em.clear();

            List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();
            Member fineMember = resultList.get(0);
            fineMember.setAge(20); // 값 변경시 update문 query문이 나감 즉슨, resultList는 영속성 콘텍스트에 의해 관리가 됨 

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
