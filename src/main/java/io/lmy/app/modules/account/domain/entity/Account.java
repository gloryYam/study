package io.lmy.app.modules.account.domain.entity;

import io.lmy.app.modules.account.domain.supprot.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;       // 이메일

    @Column(unique = true)
    private String nickname;    // 닉네임

    private String password;    // 비밀번호

    private boolean isValid;    // 인증 여부

    private String emailToken;  // 이메일 토큰

    private LocalDateTime joinedAt;

    @Embedded
    private Profile profile;

    @Embedded
    private NotificationSetting notificationSetting;

    public void generateToken() {
        this.emailToken = UUID.randomUUID().toString();
    }

    public void verified() {
        this.isValid = true;
        joinedAt = LocalDateTime.now();
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    @ToString
    public static class Profile{

        private String bio;         // 개인적인 정보를 추가하기위한 항목

        @Convert(converter = ListStringConverter.class)
        private List<String> url;           // 개인이 운영하는 웹 페이지 url
        private String job;                 // 직업
        private String location;            // 위치
        private String company;             // 회사

        @Lob @Basic(fetch = FetchType.EAGER)
        private String image;               // 프로필에 사용할 이미지
    }

    /**
     * 스터디 생성 알림
     */
    @Embeddable                                                                                         // (5)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class NotificationSetting {
        private boolean studyCreatedByEmail;
        private boolean studyCreatedByWeb;
        private boolean studyRegistrationResultByEmailByEmail;
        private boolean studyRegistrationResultByEmailByWeb;
        private boolean studyUpdatedByEmail;
        private boolean studyUpdatedByWeb;
    }
}
