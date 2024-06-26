package my.petproject.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;

@Entity
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Data
@Accessors(chain = true)
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private RoleName roleName;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName.name();
    }

    public enum RoleName {
        ADMIN,
        USER
    }

}
