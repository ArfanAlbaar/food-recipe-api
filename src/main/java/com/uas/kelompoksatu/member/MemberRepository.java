package com.uas.kelompoksatu.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findFirstByToken(String token);
}
