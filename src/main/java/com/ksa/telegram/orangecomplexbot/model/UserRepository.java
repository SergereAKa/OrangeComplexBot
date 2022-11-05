package com.ksa.telegram.orangecomplexbot.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {

}
