package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String name;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String city;

    private String street;

    private String zipcode;

}
