package coupon.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import coupon.auth.constant.RoleType;
import coupon.auth.dto.UserDto;
import coupon.auth.entity.User;
import coupon.auth.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(UserDto userDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoleType(RoleType.ADMIN);
        User user = new User(userDto);
        userRepository.save(user);
    }
}
