package com.photoApp.api.users.data;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UsersEntity, Long> {
	
	UsersEntity findByEmail(String email);

	UsersEntity findByUserId(String userId);
}
