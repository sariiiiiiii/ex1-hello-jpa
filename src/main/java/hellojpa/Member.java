package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity /* JPA가 로딩될 때 Entity클래스임을 명시 */
/*@Table(name = "USER") CLASS이름이 다를 때 이름을 변경시켜 매핑시켜줄 수 있다 */
public class Member {

    @Id /* PK */
    private Long id;

    /*@Column(name = "USER_NAME") DB랑 컬럼이름이 다를 때 변경시켜 매핑시켜줄 수 있다 */
    private String name;



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



    
}
