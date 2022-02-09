package com.nminhthang.admin.user;

import com.nminhthang.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> listAll(){
        return (List<User>) userRepository.findAll();
    }

}
