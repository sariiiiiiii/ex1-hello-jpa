package hellojpa;

import javax.persistence.*;

@Entity
public class Member2 {

    @Id @GeneratedValue
    private Long id;

    private String name;

    // FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEAM_ID")
    private Team2 team2;

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

    public Team2 getTeam2() {
        return team2;
    }

    public void setTeam2(Team2 team2) {
        this.team2 = team2;
    }
}
