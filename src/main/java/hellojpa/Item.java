package hellojpa;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn // Default로 해놓으면 자동으로 Entity 명이 자동 주입
//@DiscriminatorColumn(name = "DIS_TYPE") // DTYPE이 아닌 column명 바꾸기
public abstract class Item { // 추상클래스 생성

    /**
     * 상속 관계 매핑
     * 1. 아무것도 설정하지 않은 JPA 기본 전략방식은 한테이블에 모든 컬럼이 들어가는 방식 @Inheritance를 설정해주지 않은 방식(@Inheritance(strategy = InheritanceType.SINGLE_TABLE))
     *    ㄴ SINGLE_TABLE 전략은 @DiscriminatorColumn을 설정해주지 않아도 알아서 DTYPE이 생성된다(JOINED는 테이블이라도 다르니까 구분이 되는데 SINGLE_TABLE은 한 테이블에 들어가기 때문에 구분할 수 있는 정보가 없기 때문)
     *    ㄴ 단일테이블 전략은 insert, select QUERY문이 한번만 실행
     *    ㄴ 뭐가 되었건 간에 운영상 DTYPE은 항상 넣어주자
     *
     * 2. @Inheritance(strategy = InheritanceType.JOINED) 방식 JOIN 테이블 생성 (자식 객체 insert시 부모테이블 자동 insert, 자식테이블 조회시 ID값으로 ITEM값을 inner join)
     *
     * 3. @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 전략은 부모클래스 테이블을 생성하지 않고 그 컬럼들을 자식 테이블에 모두 생성(중복컬럼들을 다 넣어준다는 느낌)
     *    ㄴ 이 같은 경우에는 테이블이 다 분리되어져 있기 때문에 @DiscriminatorColumn은 의미가 없음 넣어도 사용이 안됨
     *    ㄴ 데이터를 넣고(insert) 조회(select)할 때에는 굉장히 편한데 부모클래스 Item을 조회할 떄는 자식테이블들을 전부 unionAll로 조회를 해버린다(매우 큰 단점)
     */

    /**
     * (실무 정석용, 설계도 잘됨)
     * JOINED 전략의 장점 : 정규화가 되어있고, 제약조건에 부모테이블에 맞춰서 걸어줄 수 있고, 저장공간 효율화
     * JOINED 전략의 단점 : 조회시 조인을 많이 사용, 성능 저하(그렇게 많이 저하되진 않음), 조회 쿼리가 복잡함, 데이터를 저장할 때 INSERT SQL 2번 호출
     *
     * SIGNLE_TABLE 장점 : 조인이 필요없어서 성능빠름, 조회 쿼리가 단순함
     * SIGNLE_TABLE 단점 : 자식 엔티티가 매핑한 컬럼은 모두 null 허용(치명적), 단일테이블에 모두 때려박기 떄문에 테이블이 커질 수 있고 상황에 따라서 오히려 성능이 저하될 수 있음
     *
     * TABLE_PER_CLASS 장점 : 없음(그냥 쓰지 말자)
     * TABLE_PER_CLASS 단점 : 객체적으로 봐도 똥이고, DB 관점으로 봐도 똥임
     */

    /**
     * class level에 @DiscriminatorColumn을 추가해주게 되면 부모테이블에 DTYPE 컬럼 생성
     * @DiscriminatorColumn(name = "COLUMN_NAME") 부모레벨에서 선언을 해주면 테이블 컬럼명을 수정해줄 수 있다
     * @DiscriminatorValue("COLUMN_NAME") 자식클래스 클래스레벨에 선언해주게 되면 DTYPE에 들어가는 값이 Entity명이 아니라 수정해줄 수 있다
     */

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
