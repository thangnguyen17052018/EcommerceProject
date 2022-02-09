package com.nminhthang.admin.user;

import com.nminhthang.common.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
