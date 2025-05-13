package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom{
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    List<User> findAllByStatusAndDueDateBefore(Status status, LocalDate date);
}