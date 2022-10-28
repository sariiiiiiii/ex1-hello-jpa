package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//@Entity
@MappedSuperclass
public abstract class BaseEntity {

    // abstract(상속관계) 클래스
    // @Entity가 있을 경우는 상속매핑
    // @MappedSuperclass가 있을경우는 속성만

    /**
     * @MappedSuperclass
     * 상속관계와 상관없이 속성을 같이 쓰고 싶을 때 사용
     * class level에 선언
     * 속성에 @Column annotation 가능
     * Entity가 아니여서 table과 매핑이 되지 않는다(table이 생성되지 않음)
     * 조회, 검색 불가(em.find(BaseEntity)불가)
     * 부모클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
     * 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
     */

    @Column(name = "INSERT_MEMBER")
    private String createdBy;

    private LocalDateTime createdDate;

    @Column(name = "UPDATE_MEMBER")
    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
