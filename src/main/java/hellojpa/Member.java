package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity /* JPA가 로딩될 때 Entity클래스임을 명시 */
/*@Table(name = "USER") CLASS이름이 다를 때 이름을 변경시켜 매핑시켜줄 수 있다 */
public class Member {

        public Member(){}

        @Id
        private Long id;

        @Column(name = "name") /* column의 name값을 임의로 맞춰줄 수 있다 */
        private String username;

        private Integer age; /* 다른 타입도 가능 */

        @Enumerated(EnumType.STRING)
        private RoleType roleType;

        @Temporal(TemporalType.TIMESTAMP)
        private Date createdDate;

        @Temporal(TemporalType.TIMESTAMP)
        private Date lastModifiedDate;

        @Lob
        private String description;

    /*================================================================================================*/

//    @Id /* PK */
//    private Long id;
//
//    /*@Column(name = "USER_NAME") DB랑 컬럼이름이 다를 때 변경시켜 매핑시켜줄 수 있다 */
//    /*@Column(unique = true, length = 10) 애플리케이션 생성될 때 JPA 생성, 실행자체에는 영향을 주지않고 DDL 생성시에만 영향을 준다(DDL 생성기능) */
//    private String name;
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    /*================================================================================================*/

}
