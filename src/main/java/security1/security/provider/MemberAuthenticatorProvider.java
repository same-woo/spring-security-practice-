package security1.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import security1.member.entity.Member;
import security1.security.auth.MemberPrincipalDetailService;
import security1.security.auth.MemberPrincipalDetails;

@Component
// AuthenticationProvider 를 구현한 클래스
public class MemberAuthenticatorProvider implements AuthenticationProvider {

    @Autowired
    private MemberPrincipalDetailService memberPrincipalDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // 사용자가 입력한 id
        String password = (String) authentication.getCredentials(); // 사용자가 입력한 password

        // 생성해둔 MemberPrincipalDetailService 에서 loadUserByUsername 메소드를 호출하여 사용자 정보를 가져온다.
        MemberPrincipalDetails memberPrincipalDetails = (MemberPrincipalDetails) memberPrincipalDetailService.loadUserByUsername(username);

        // ====================================== 비밀번호 비교 ======================================
        // 사용자가 입력한 password 와 DB 에 저장된 password 를 비교한다.

        // db 에 저장된 password
        String dbPassword = memberPrincipalDetails.getPassword();
        // 암호화 방식 (BCryptPasswordEncoder) 를 사용하여 비밀번호를 비교한다.
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(password, dbPassword)) {
            System.out.println("[사용자] 비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("[사용자] 아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        // ========================================================================================

        // 사용자가 입력한 id 와 password 가 일치하면 인증이 성공한 것이다.
        // 인증이 성공하면 MemberPrincipalDetails 객체를 반환한다.
        Member member = memberPrincipalDetails.getMember();
        if (member == null || "N".equals(member.getIsUsed())) {
            System.out.println("[사용자] 사용할 수 없는 계정입니다.");
            throw new BadCredentialsException("[사용자] 사용할 수 없는 계정입니다.");
        }

        // 인증이 성공하면 UsernamePasswordAuthenticationToken 객체를 반환한다.
        // 해당 객체는 Authentication 객체로 추후 인증이 끝나고 SecurityContextHolder.getContext() 에 저장된다.
        return new UsernamePasswordAuthenticationToken(memberPrincipalDetails, null, memberPrincipalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
