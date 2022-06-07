package hellojpa;

import org.w3c.dom.ls.LSOutput;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    /** mappedBy의 정체
     * 주인은 mappedBy 사용 X
     * mappedBy의 뜻 내가 저거의 의해서 mapping이 되었다
     * Team객체를 주인으로 했을 때 List<Member> members를 수정하게 될 경우 Member 객체의 update쿼리가 날라간다 (뭔가 이상함) + 성능 issue
     */
    // 나는 team의 의해서 관리가 됨(Member의 team 객체)
    @OneToMany(mappedBy = "team") // 나의 반대편 객체에는 뭐랑 걸려있는지 mappedBy로 명시 // 나는 "team"으로 매핑되어있다
    private List<Member> members = new ArrayList<>(); // ArrayList로 초기화 하는 이유는 add할 때 NULLPOINT를 안뜨게 하기 위함

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
