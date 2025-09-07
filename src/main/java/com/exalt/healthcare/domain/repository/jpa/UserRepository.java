package com.exalt.healthcare.domain.repository.jpa;

import com.exalt.healthcare.domain.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends EntityRepository<User, Long>{
}
