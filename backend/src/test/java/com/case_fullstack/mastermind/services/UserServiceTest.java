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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        UserCreateDTO dto = new UserCreateDTO(
                "breno",
                "breno@email.com",
                "123456"
        );

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponseDTO response = userService.createUser(dto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("breno", response.username());
        assertEquals("breno@email.com", response.email());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("breno", savedUser.getUsername());
        assertEquals("breno@email.com", savedUser.getEmail());
        assertEquals("123456", savedUser.getPassword());
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        UserCreateDTO dto = new UserCreateDTO(
                "breno",
                "novo@email.com",
                "123456"
        );

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("breno");
        existingUser.setEmail("outro@email.com");
        existingUser.setPassword("abc");

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(existingUser));

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(dto));

        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        UserCreateDTO dto = new UserCreateDTO(
                "breno",
                "breno@email.com",
                "123456"
        );

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("outroUser");
        existingUser.setEmail("breno@email.com");
        existingUser.setPassword("abc");

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(dto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {
        UserLoginDTO dto = new UserLoginDTO("breno", "123456");

        User user = new User();
        user.setId(1L);
        user.setUsername("breno");
        user.setEmail("breno@email.com");
        user.setPassword("123456");

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.loginUser(dto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("breno", response.username());
        assertEquals("breno@email.com", response.email());
    }

    @Test
    void shouldThrowWhenLoginUserDoesNotExist() {
        UserLoginDTO dto = new UserLoginDTO("breno", "123456");

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.loginUser(dto));
    }

    @Test
    void shouldThrowWhenPasswordIsInvalid() {
        UserLoginDTO dto = new UserLoginDTO("breno", "senhaErrada");

        User user = new User();
        user.setId(1L);
        user.setUsername("breno");
        user.setEmail("breno@email.com");
        user.setPassword("123456");

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));

        assertThrows(InvalidPasswordException.class, () -> userService.loginUser(dto));
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("breno");
        user.setEmail("breno@email.com");
        user.setPassword("123456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("breno", result.getUsername());
        assertEquals("breno@email.com", result.getEmail());
    }

    @Test
    void shouldThrowWhenFindingUserByIdAndUserDoesNotExist() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
    }
}
