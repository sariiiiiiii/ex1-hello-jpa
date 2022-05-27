package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
public class Employee {

    public Employee(){}

    @Id
    private String id;
    
    /* @Id 내가 PK값을 직접 setting할 때 */

    /* @Id @GeneratedValue.AUTO는 PK값 오라클 = 시퀀스, mySQL = auto.increment처럼 DB방언에 맞춰서 자동으로 생성 */
    /* @ID @GeneratedValue.INDENTITY 는 PK값을 DB에 맡긴다 */
    
    /* JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL을 실행한다 */
    /* AUTO_INCREMENT는 데이터베이스에 INSERT SQL을 실행한 이후에 ID값을 알 수 있음 */
    /* IDENTITY 전략은 em.persist()시점에 즉시 INSERT_SQL 실행하고 DB에서 식별자를 조회 */
    /* SEQUENCE 전략은  SEQUENCE OBJECT를 먼저 생성하고 값을 넣는거기 때문에 */
    
    /* INDENTITY 전략은 DB에서 ID값이 null로 날아오면 그 때 DB에서 값을 넣어준다 즉슨, DB를 들어가봐야 값을 알 수 있다 */
    /* 영속성 콘텍스트는 id값이 있어야 한다(같은 트랜잭션안에 돌아갈 때) 근데 DB에 들어가봐야 값을 알 수 있으니까 */
    /* 그래서, 1차 캐시에서는 key가 없으니까 JPA는 알수가 없다 그래서, INDENTITY는 persist 시점에 바로 insert query가 날라간다 */
    /* 그리고 JPA가 pk값을 바로 가져온다 */
    /* 그래서 모아서 INSERT하는게 INDENTITY 전략에서는 안좋다 */
    /* 그 외에는 commit하는 시점에 insert query발송 */
    
}
