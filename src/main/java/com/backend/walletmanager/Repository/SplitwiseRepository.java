package com.backend.walletmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.walletmanager.entity.Splitwise;

public interface SplitwiseRepository extends JpaRepository<Splitwise, Integer> {

}
