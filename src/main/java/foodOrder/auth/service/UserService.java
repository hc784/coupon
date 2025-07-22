package foodOrder.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import foodOrder.auth.constant.RoleType;
import foodOrder.auth.dto.UserDto;
import foodOrder.auth.entity.User;
import foodOrder.auth.repository.UserRepository;

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
