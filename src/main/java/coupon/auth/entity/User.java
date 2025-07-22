package coupon.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import coupon.auth.constant.RoleType;
import coupon.auth.dto.UserDto;

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

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

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
        this.department = dto.getDepartment();
        this.position = dto.getPosition();
        this.roleType = dto.getRoleType();
    }
}
