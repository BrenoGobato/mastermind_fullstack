package com.case_fullstack.mastermind.services;

import com.case_fullstack.mastermind.infra.exceptions.EmailAlreadyExistsException;
import com.case_fullstack.mastermind.infra.exceptions.InvalidPasswordException;
import com.case_fullstack.mastermind.infra.exceptions.UserNotFoundException;
import com.case_fullstack.mastermind.infra.exceptions.UsernameAlreadyExistsException;
import com.case_fullstack.mastermind.models.dtos.UserCreateDTO;
import com.case_fullstack.mastermind.models.dtos.UserLoginDTO;
import com.case_fullstack.mastermind.models.dtos.UserResponseDTO;
import com.case_fullstack.mastermind.models.entities.User;
import com.case_fullstack.mastermind.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {

        //Validating if username and email already exists
        if(userRepository.findByUsername(userCreateDTO.username()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.findByEmail(userCreateDTO.email()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        //Setting values of a new user
        User user = new User();
        user.setUsername(userCreateDTO.username());
        user.setEmail(userCreateDTO.email());
        user.setPassword(userCreateDTO.password());

        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponseDTO loginUser(UserLoginDTO userLoginDTO){
        //Validating username registered earlier
        String username =  userLoginDTO.username();
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        //Validating if password matches the username
        if(!user.getPassword().equals(userLoginDTO.password())){
            throw new InvalidPasswordException();
        }

        //Returns a DTO for login
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
