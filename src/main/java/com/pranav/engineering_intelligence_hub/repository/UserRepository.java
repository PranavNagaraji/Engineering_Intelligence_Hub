package com.pranav.engineering_intelligence_hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pranav.engineering_intelligence_hub.entity.Role;
import com.pranav.engineering_intelligence_hub.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

    @Query("""
		SELECT u FROM User u
		""")
    List<User> findAllUsersCustom();

    @Query("""
		Select u.username
		FROM User u
		""")
    List<String> findAllUsernames();

	@Modifying(
        clearAutomatically = true,
        flushAutomatically = true
    )
	@Query("""
		update User u
		set u.role=:role
		where u.id=:id
		""")
	int updateUserRole(@Param("id") Long id, @Param("role") Role role);	

	@Query(
		value="""
			SELECT * FROM users
			WHERE role = :role
			""",
		nativeQuery = true
	)
	List<User> findUsersByRole(@Param("role") Role role);
}
