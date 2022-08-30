package com.example.demo.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long>{
	
	Optional<ConfirmationToken> findByToken(String token);

	 @Transactional
	 @Modifying
	 @Query("update ConfirmationToken u set u.comfirmedAt = :comfirmedAt where u.token = :token")
	 int updateConfirmedAt(@Param("comfirmedAt") LocalDateTime comfirmedAt, 
	   @Param("token") String token);

}
