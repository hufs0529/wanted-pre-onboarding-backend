package wanted.wantedpreonboardingbackend.domain.member.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Setter
@Getter
@Table(name="member")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}
