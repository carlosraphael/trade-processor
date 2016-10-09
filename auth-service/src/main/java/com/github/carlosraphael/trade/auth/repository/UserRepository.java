package com.github.carlosraphael.trade.auth.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.github.carlosraphael.trade.auth.model.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	User findByEmail(String email);
}