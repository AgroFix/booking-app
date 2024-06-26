package my.petproject.booking.repository;

import java.util.Optional;
import my.petproject.booking.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findUserByEmail(String email);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findUserById(Long id);
}
