package security1.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // 엔티티 생성시
@Getter
@Setter
@Builder // 추후 Builder 사용시
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "MEMBER_TB")
public class Member {

    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "MEMBER_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @Column(name = "MEMBER_LOGIN_ID")
    private String loginId;

    @Column(name = "MEMBER_ROLE")
    private String role;

    @Column(name = "MEMBER_NAME")
    private String name;

    @Column(name = "MEMBER_PASSWORD")
    private String password;

    @Column(name = "MEMBER_EMAIL")
    private String email;

    @Column(name = "IS_USED")
    private String isUsed;

    @Column(name = "IS_DEL")
    private String isDel;

    @Column(name = "ISRT_DATE")
    private LocalDateTime isrtDate;

    @Column(name = "UPDT_DATE")
    private LocalDateTime updtDate;
}