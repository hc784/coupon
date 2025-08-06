package foodOrder.auth.v1.service;

import foodOrder.auth.entity.Users;
import foodOrder.auth.security.CustomUserPrincipal;
import foodOrder.auth.v1.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	  Users user = usersRepository.findByEmail(username)
                  .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

          return CustomUserPrincipal.from(user);   // ★ 엔티티 대신 어댑터 반환
      
    }
}
