package com.utcn.ds.chatmanagement.repository;


import com.utcn.ds.chatmanagement.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Searches a user by email in database.
     *
     * @param email the email to search for
     * @return an optional containing the user object that matches the specified email or an empty optional if no match is found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the specified email exists in the database.
     *
     * @param email the email to check for
     * @return true if a user with the specified email exists, false otherwise
     */
    boolean existsByEmail(String email);

    List<User> findAllByRole(String role);

    void deleteById(Long id);
}
