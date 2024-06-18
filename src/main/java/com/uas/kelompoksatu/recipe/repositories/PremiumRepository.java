package com.uas.kelompoksatu.recipe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.recipe.entities.PremiumResponse;

@Repository
public interface PremiumRepository extends JpaRepository<Premium, Integer> {

    List<PremiumResponse> findAllBy();

    Optional<PremiumResponse> findFirstById(Integer id);
}
