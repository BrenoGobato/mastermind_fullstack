package com.case_fullstack.mastermind.services;

import com.case_fullstack.mastermind.models.dtos.UserCreateDTO;
import com.case_fullstack.mastermind.models.dtos.UserLoginDTO;
import com.case_fullstack.mastermind.models.dtos.UserResponseDTO;
import com.case_fullstack.mastermind.models.entities.User;
import com.case_fullstack.mastermind.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        User user = new User();

        if(userRepository.findByUsername(userCreateDTO.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }else{
            user.setUsername(userCreateDTO.username());
        }

        if (userRepository.findByEmail(userCreateDTO.email()) != null) {
            throw new RuntimeException("Email already exists");
        }else {
            user.setEmail(userCreateDTO.email());
        }

        user.setPassword(userCreateDTO.password());

        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponseDTO loginUser(UserLoginDTO userLoginDTO){
        String username =  userLoginDTO.username();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getPassword().equals(userLoginDTO.password())){
            return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
        }
        else {
            throw new RuntimeException("Invalid password");
        }
    }

    public User findUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        return user;
    }
}
