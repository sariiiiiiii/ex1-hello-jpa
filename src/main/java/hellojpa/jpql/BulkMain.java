package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static hellojpa.jpql.MemberType.ADMIN;

public class BulkMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            Member memberA = new Member();
            memberA.setAge(10);
            memberA.setUsername("memberA");
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setAge(12);
            memberB.setUsername("memberB");
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setAge(14);
            memberC.setUsername("memberC");
            em.persist(memberC);

            /**
             * 벌크 연산
             * 쉽게 말해 update, delete라고 생각하면 된다
             * PK값을 찍어 단건을 update, delete하는것을 제외한 나머지 모든 update, delete라고 생각하면 됨
             */

            /**
             * 재고가 10개 미만인 모든 상품의 가격을 10% 상승시키려면?
             * JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL 실행
             *   1. 재고가 10 미만인 상품을 리스트로 조회한다
             *   2. 상품 엔티티의 가격을 10% 증가한다
             *   3. 트랜잭션 커밋 시점에 변경감지가 동작한다
             * 변경된 데이터가 100건이라면 100번의 UPDATE SQL실행 된다
             */

            /**
             * 쿼리 한 번으로 여러 테이블 로우 변경(엔티티)
             * executeUpdate()의 결과는 영향받은 엔티티 수 반환
             * UPDATE, DELETE 지원
             * INSERT(insert into .. select, 하이버네이트 지원)
             */

            // executeUpdate()
            // 벌크연산 전 FLUSH 자동 호출 (flush 시점 = commit, query실행, 강제 flush()
            int resultCount = em.createQuery("update Member m set m.age = 20 where m.age = 10")
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);

            // 분명 update를 했는데 영속성 컨텍스를 보니까 아직도 나이가 10인걸 확인할 수 있다 벌크 연산후에는 꼭 영속성 컨텍스트 초기화를 해주자
            System.out.println(memberA.getAge());
            System.out.println(memberB.getAge());
            System.out.println(memberC.getAge());

            // 위와 같은 이유로 다시 조회해봤는데 영속성 초기화를 안해주면 원래있던 정보를 불러오기 때문에 아직도 update된 정보를 가져올 수 없다
            Member findMember = em.find(Member.class, memberA.getId());
            System.out.println(findMember.getAge());

            em.clear(); // 영속성 초기화

            // 초기화를 시켜주고 나니까 제대로된 정보를 가져온다....
            Member findMember2 = em.find(Member.class, memberA.getId());
            System.out.println(findMember2.getAge());


            /**
             * 벌크 연산 주의
             * 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스의 직접 쿼리
             *   ㄴ 벌크 연산 먼저 실행 (영속성 컨텍스트 저장되기 전에 벌크연산 먼저 실행)
             *   ㄴ 벌크 연산 수행 후 영속성 컨텍스트 초기화 !! (영속성 컨테스트 query가 실행되는거라 벌크 연산 수행전에는 flush가 당연히 나가서 상관없는데 연산 후 영속성 컨텍스트를 초기화 해주자)
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
