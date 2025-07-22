package foodOrder.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import foodOrder.auth.constant.RoleType;
import foodOrder.auth.dto.UserDto;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "email", "email", "department", "position", "roleType", "createdBy", "modifiedBy" })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String createdBy;
    private LocalDateTime createdDt;
    private String modifiedBy;
    private LocalDateTime modifiedDt;

    public User(UserDto dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.username = dto.getUsername();
        this.roleType = dto.getRoleType();
    }
}
