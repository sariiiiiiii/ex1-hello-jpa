package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CollectionTypeJpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 값 타입 컬렉션
             */
            Customer customer = new Customer();
            customer.setName("customerA");
            customer.setHomeAddress(new Address("homeCity", "homeStreet", "homeZipcode"));

            // 값 타입 SET
            customer.getFavoriteFoods().add("치킨");
            customer.getFavoriteFoods().add("족발");
            customer.getFavoriteFoods().add("피자");

            // 값 타입 LIST
//            customer.getAddressHistory().add(new Address("old1", "street1", "zipcode1"));
//            customer.getAddressHistory().add(new Address("old2", "street2", "zipcode2"));
//            customer.getAddressHistory().add(new Address("old3", "street3", "zipcode3"));

            // addressEntity를 활용한 방법
            customer.getAddressEntity().add(new AddressEntity("old1", "street1", "zipcode1"));
            customer.getAddressEntity().add(new AddressEntity("old2", "street2", "zipcode2"));
            customer.getAddressEntity().add(new AddressEntity("old3", "street3", "zipcode3"));

            /**
             * 컬렉션은 다른 테이블인데도 불가하고 라이프사이클이 같이 돌았다 (em.persist(customer))만 해주었을 뿐인데 insert query문이 나감
             * 왜냐 ? 값 타입이기 때문(값 타입은 따로 라이프사이클이 없다, 갑 타입의 생명주기는 해당 domain class에 의존한다)
             * 참고 : 값 타입 컬렉션은 영속성 전이(cascadeType) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있음
             */

            em.persist(customer);

            // 영속성 초기화
            em.flush();
            em.clear();

            System.out.println("========================= 조회 START ==========================");
            // 값을 조회했을 때 순수 Customer만 조회된다(@Embedded 포함)
            // 값 타입 컬렉션들은 default 지연로딩이기 때문에 조회 X
            Customer findCustomer = em.find(Customer.class, customer.getId());

            // 지연로딩이므로 이 때 조회
            List<AddressEntity> findCustomerAddressHistory = findCustomer.getAddressEntity();
            for (AddressEntity a : findCustomerAddressHistory) {
                System.out.println("address = " + a.getAddress().getCity());
            }

            // 지연로딩이므로 이 때 조회
            Set<String> findFavoriteFood = findCustomer.getFavoriteFoods();
            for (String food : findFavoriteFood) {
                System.out.println("food = " + food);
            }
            System.out.println("========================== END ==========================");

            /**
             * 값 타입 컬렉션의 제약사항
             * 1. 값 타입은 엔티티와 다르게 식별자 개념이 없다
             * 2. 값은 변경하면 추적이 어렵다
             * 3. 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다
             * 4. 값 타입 컬렉션을 매핑하는 테이블은 모두 컬럼을 묶어서 기본키를 구성해야 함: null 입력 X, 중복 저장X
             */

            // homeCity => newCity (수정)
//            findCustomer.setHomeAddress("newCity"); X
            Address oldAddress = findCustomer.getHomeAddress();
            findCustomer.setHomeAddress(new Address("newCity", oldAddress.getStreet(), oldAddress.getZipcode())); // 통으로 새로 갈아끼우기

            // 치킨 => 한식 (SET)
            // String이므로 값 자체를 갈아끼워야 한다(update 불가능)
            // 컬렉션에 값만 변경해도 JPA 알아서 값을 변경
            findCustomer.getFavoriteFoods().remove("치킨");
            findCustomer.getFavoriteFoods().add("한식");

            /**
             * address query를 보니까 address table의 PK값 데이터들을 다 지우고 새로 insert를 하는것을 볼 수 있다
             * 결론적으로는 DB의 데이터가 수정된 것을 볼수는 있는데 delete ALL 때리고 insert문을 3번을 날리네...
             */
            // old1 => new1 (LIST)
            // 기본적으로 collection은 대상을 찾을 때 equals()를 사용
            // equals()랑 hashCode()를 제대로 구현하지 않으면 값을 제대로 찾기 어렵다 제대로 구현해놓자 !!
//            findCustomer.getAddressEntity().remove(new Address("old1", "street1", "zipcode1"));
//            findCustomer.getAddressEntity().add(new Address("newCity1", "street1", "zipcode1" ));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
