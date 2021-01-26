package ru.awg.rupost.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.awg.rupost.demo.dao.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByToken(String token);

    @Modifying
    @Query("UPDATE UserEntity u SET u.token = :token WHERE u.id = :id")
    int updateUserTokenById(Long id, String token);
}