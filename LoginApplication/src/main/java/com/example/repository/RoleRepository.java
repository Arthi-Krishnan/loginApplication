package com.example.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Role;
import com.example.entity.User;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
	
	@Query("SELECT u FROM Role u WHERE u.id = :id")
    public Role getRoleById(@Param("id") Integer id);	

}
