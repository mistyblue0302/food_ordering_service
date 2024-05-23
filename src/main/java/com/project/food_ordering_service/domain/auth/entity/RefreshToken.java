package com.project.food_ordering_service.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Entity : JPA 엔티티 클래스임을 나타내는 애노테이션으로 데이터베이스 테이블과 매핑되는 클래스임을 표시
 * @Getter : Lombok 라이브러리에서 제공하는 애노테이션으로 getter 메소드를 자동으로 생성
 * @Builder : 빌더 패턴을 사용할 수 있게 도와주는 애노테이션
 * @NoArgsConstructor : 필드가 없는 기본 생성자를 만들어준다.
 * @AllArgsConstructor : 모든 필드를 받는 생성자를 만들어준다.
 */

@Getter
@Entity
@Table(name = "LOGOUT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expiredDate;
}
