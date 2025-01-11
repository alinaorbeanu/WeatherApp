package com.utcn.ds.usermanagement.repository;

import com.utcn.ds.usermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Long> {
}
