package com.backend.walletmanager.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.walletmanager.entity.ExternalAccounts;

public interface ExternalAccountsRepository extends JpaRepository<ExternalAccounts, Integer> {

	@Query(value = "SELECT * FROM external_accounts WHERE user_id = ?1", nativeQuery = true)
	Optional<ExternalAccounts> findByUserId(Integer userId);

	@Query(value = "SELECT * FROM external_accounts WHERE user_id = ?1", nativeQuery = true)
	List<ExternalAccounts> findAllByUserId(Integer userId);

}
