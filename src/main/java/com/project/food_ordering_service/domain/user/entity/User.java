package com.project.food_ordering_service.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Entity : JPA 엔티티 클래스임을 나타내는 애노테이션으로 데이터베이스 테이블과 매핑되는 클래스임을 표시
 * @Getter : Lombok 라이브러리에서 제공하는 애노테이션으로 getter 메소드를 자동으로 생성
 * @Builder : 빌더 패턴을 사용할 수 있게 도와주는 애노테이션
 * @NoArgsConstructor : 필드가 없는 기본 생성자를 만들어준다.
 * @AllArgsConstructor : 모든 필드를 받는 생성자를 만들어준다.
 */

@Getter
@Entity
@Table(name = "USERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "username")
    private String userName;

    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "create_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    public void modify(String name, String phoneNumber, String modifiedBy) {
        this.userName = name;
        this.phoneNumber = phoneNumber;
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = modifiedBy;
    }
}
