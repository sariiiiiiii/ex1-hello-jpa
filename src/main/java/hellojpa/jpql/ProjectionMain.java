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

            /**
             * 프로젝션 여러값 조회
             * SELECT m FROM Member AS m
             * 1. Query 타입으로 조회
             * 2. Object[]으로 조회
             * 3. new 명령어로 조회
             *    ㄴ 단순 값을 DTO로 바로 조회
             *       ㄴ select new jpabook jpql UserDTO(m.username, m.age) from Member m
             *    ㄴ 패키지 명을 포함한 전체 클래스 명 입력
             *    ㄴ 순서와 타입이 일치하는 생성자 필요
             */

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            em.persist(member);
            
            em.flush();
            em.clear();

            // 엔티티 프로젝션
            List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();
            Member fineMember = resultList.get(0);
            fineMember.setAge(20); // 값 변경시 update문 query문이 나감 즉슨, resultList는 영속성 콘텍스트에 의해 관리가 됨

            // 임베디드 프로젝션
            // 임베디드 프로젝션 개별의 Entity가 아니고 Entity에 소속되어 있기 때문에 "select a from Address as a" 이렇게 접근이 불가능
            List<Address> addressList = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            // 스칼라타입 프로젝션
            List memberList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            // Object를 이용한 방법 (Type을 명시를 못하니까 Object로 돌린다)
            Object o = memberList.get(0);
            Object [] result = (Object[]) o;
            System.out.println(result[0]);
            System.out.println(result[1]);

            List<Object[]> memberList1 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            Object[] result1 = memberList1.get(0);
            System.out.println(result1[0]);
            System.out.println(result1[1]);

            // new 명령어로 조회(실무 권장)
            // MemberDTO는 Entity가 아니다 Entity가 아닌 다른 경우로 갈 경우(new operation 사용, package명을 다 적어줘야됨) 생성자를 호출하듯이
            List<MemberDTO> resultList2 = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList2.get(0);
            System.out.println("username = " + memberDTO.getUsername());
            System.out.println("age = " + memberDTO.getAge());

            et.commit();

        } catch (Exception e) {
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
