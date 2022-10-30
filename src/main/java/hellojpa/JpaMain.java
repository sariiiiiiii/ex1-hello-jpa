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
             * 영속성 전이 CASCADE
             * 특정 엔티티를 영속 상태로 만들고 싶을 때, 연관된 엔티티도 함께 영속상태로 만들고 싶을 때
             * 예) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장
             *
             * 실무에서는 왠만하면 이 2가지 위주로 사용 !
             * cascade = CascadeType.ALL = 모두 적용
             * cascade = CascadeType.PERSIST = 영속 (저장할 때만 사용)
             *
             * 사용할 예시 : 첨부파일 - 첨부파일의 경로는 한 게시물에서만 관리를 하기 떄문 (서로만 연관관계가 되어 있을 때, 소유자가 하나일 때)
             * 안되는 예시 : 그 파일을 다른 게시판이나 다른 엔티티에서도 관리를 할 때
             *
             * 주의할 점 !
             * 1. 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
             * 2. 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐 그 이상 그 이하도 아님
             */

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            // Parent 도메인에 cascade = Cascade = ALL 추가하니 child도 함께 insert
            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            em.flush();
            em.clear();

            /**
             * 고아 객체
             * orphanRemoval = true
             *
             * 주의
             * 1. 참조가 제거된 엔티티는 다른 곳에서 참조하지 않은 고아 객체로 보고 삭제하는 기능
             * 2. 참조하는 곳이 하나일 때 사용해야 함 !
             * 3. 특정 엔티티가 개인 소유할 때 사용
             * 4. @OneToOne, @OneToMany만 가능
             * 참고 : 개념적으로 부모를 제거하면 자식은 고아가 된다 따라서, 고아 객체 제거 기능을 활성화 하면 부모를 제거할 때 자식도 함께 된다
             *       이것은 CascadeType.REMOVE처럼 동작한다
             */

            // orphanRemoval = true를 걸어준 @OneToMany List에서 삭제된 객체는 자동으로 Delete 된다
            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);

            /**
             * cascadeType.ALL 과 orphanRemoval = true를 동시에 하게 될 겨우
             * parent(부모 entity)의 생명주기는 JPA가 하고 있지만 child(자식 entity)의 생명주기는 parent(부모 entity)가 관리하게 됨
             * 즉, 자식 entity의 DAO나 repository는 의미가 없어지게 됨
             * 도메인 주도 설게(DDD)의 Arggregate Root 개념을 구현할 때 유용
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
