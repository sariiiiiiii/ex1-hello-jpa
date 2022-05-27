package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//@Entity /* JPA가 로딩될 때 Entity클래스임을 명시 */
/*@Table(name = "USER") CLASS이름이 다를 때 이름을 변경시켜 매핑시켜줄 수 있다 */
public class Member {

        public Member(){}

        @Id
        private Long id;

        /*unique = true 는 DB에서 이름이 랜덤으로 생성되기 때문에 class 어노테이션에서 @Table(uniqueConstraints = '이름' 설정 가능ㄴ*/
        @Column(name = "name", nullable = false) /* column의 name값을 임의로 맞춰줄 수 있다 */
        private String username;

//        private Integer age; /* 다른 타입도 가능 */
        private int age;

//        @Enumerated(EnumType.STRING) /* DB에서 VARCHAR랑 매핑 */
//        private RoleType roleType;

        @Temporal(TemporalType.TIMESTAMP)
        private Date createdDate;

        @Temporal(TemporalType.TIMESTAMP)
        private Date lastModifiedDate;

        /* 자바 8 이후로부터는 @Temporal 없이 LocalDateTime클래스로 생성하자 */
        private LocalDate testLocalDate;

        private LocalDateTime testLocalDateTime;

        @Lob /* 문자타입이면은 DB에서 CLUB으로 생성 아니면 LOB타입 */
        private String description;

        @Transient /* 메모리에서만 계산되고 DB를 신경쓰고 싶지 않을 때 (DB에도 생성되지 않음) */
        private int temp;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public int getAge() {
                return age;
        }

        public void setAge(int age) {
                this.age = age;
        }

        public Date getCreatedDate() {
                return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
                this.createdDate = createdDate;
        }

        public Date getLastModifiedDate() {
                return lastModifiedDate;
        }

        public void setLastModifiedDate(Date lastModifiedDate) {
                this.lastModifiedDate = lastModifiedDate;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public int getTemp() {
                return temp;
        }

        public void setTemp(int temp) {
                this.temp = temp;
        }

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
