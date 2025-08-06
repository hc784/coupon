package foodOrder.auth.security;

import foodOrder.auth.entity.Users;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    /** 엔티티 → 프린서플 변환 */
    public static CustomUserPrincipal from(Users user) {
        List<GrantedAuthority> auth =
                user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new CustomUserPrincipal(
                user.getIdx(),
                user.getEmail(),
                user.getPassword(),
                auth
        );
    }

    public Long getId() { return id; }
    /* UserDetails 필수 메서드 */
    @Override public String getUsername()              { return email; }
    @Override public String getPassword()              { return password; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
