package security1.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security1.member.entity.Member;
import security1.member.repository.MemberRepository;
import security1.security.auth.MemberPrincipalDetails;

// UserDetailsService 를 구현한 클래스
// 스프링 시큐리티가 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
// password 부분 처리는 알아서 함
@Service
public class MemberPrincipalDetailService implements UserDetailsService {

    // JPARepository 를 상속받은 인터페이스를 자동으로 DI (의존성 주입)
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 넘겨받은 id 로 DB 에서 회원 정보를 찾음
        Member member = memberRepository.findByLoginId(username);
        System.out.println("username : " + username);
        System.out.println("member : " + member);
        // 없을경우 에러 발생
        if(member == null)
            throw new UsernameNotFoundException(username + "을 찾을 수 없습니다.");

        if(!"Y".equals(member.getIsUsed()))
            throw new UsernameNotFoundException("사용할 수 없는 계정입니다.");

        if(!"N".equals(member.getIsDel()))
            throw new UsernameNotFoundException("삭제된 계정입니다.");

        // MemberPrincipalDetails 에 Member 객체를 넘겨줌
        return new MemberPrincipalDetails(member);
    }
}