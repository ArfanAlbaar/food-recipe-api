package com.uas.kelompoksatu.recipe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.user.User;

@Repository
public interface PremiumRepository extends JpaRepository<Premium, Integer> {
    Optional<Premium> findFirstByUserAndId(User user, Integer id);
}
