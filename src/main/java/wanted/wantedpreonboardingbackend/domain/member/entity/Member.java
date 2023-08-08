package wanted.wantedpreonboardingbackend.domain.member.entity;

import javax.persistence.Id;

public class Member {

    @Id
    private Long id;

    private String email;

    private String pasword;
}
