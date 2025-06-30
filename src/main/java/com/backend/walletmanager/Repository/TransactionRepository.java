package com.backend.walletmanager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.walletmanager.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query(value = "SELECT * FROM transactions WHERE   user_user_id = ?1", nativeQuery = true)
    List<Transaction> findByUserId(Integer userId);
    
}
