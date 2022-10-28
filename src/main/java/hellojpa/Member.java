package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//        @Column(name = "TEAM_ID")
//        private Long teamId;

    /**
     * ManyToOne(다 쪽이) 연관관계의 주인이 되는걸 약속하자 (DB의 테이블에서 N쪽인 것이 연관관계의 주인)
     */
    // OWNER (연관관계의 주인)
    // 객체의 Team객체와 DB의 TEAM_ID(PK)를 매핑
    @ManyToOne // Member 입장에서는 many이고 Team 입장에서는 One이기 때문에 ManyToOne
    // join을 해야 되는 컬럼이 뭐냐
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    // 일대일[1:1] 매핑 (연관관계의 주인)
    // 대상 테이블에 왜래키가 있는 단방향 매핑은 JPA에서 지원 X (양방향 관계는 지원)
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    // ============================= [ N : M ] ======================================
    // @JoinTable로 table 생성
    // PK가 JOINTABLE로 FK로 변환되서 insert

//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT")
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    // ===============================================================================

    /**
     * 1:N 일 때 Member에서 Team 조회(단순 조회) 공식적으로 존재 X
     * @ManytoOne
     * @JoinColumn(name = "TEAM_ID", insertTable = false, updateTable = false)
     * private Team team
     * 결론은 다대일 양방향을 사용하자 (객체적으로 좀더 손해를 보긴 하긴 하지만 사용 !!)
     */

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     *  라이브러리를 이용한 무한루프 경우
     *  member에 toString생성할 경우 team.toString을 호출한다(team에서 toString을 생성했을 경우)
     *  양쪽으로 계속 호출하기 때문에 stackOverFlow 발생
     */
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", team=" + team +
                '}';
    }
}
