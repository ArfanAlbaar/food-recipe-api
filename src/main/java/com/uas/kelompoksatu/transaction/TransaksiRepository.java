package com.uas.kelompoksatu.transaction;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uas.kelompoksatu.user.User;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Integer> {
    Optional<Transaksi> findFirstByUserAndId(User user, Integer id);
}
