package com.backend.projetointegrador.services;

import com.backend.projetointegrador.domain.dtos.UserRequestDTO;
import com.backend.projetointegrador.domain.dtos.UserResponseDTO;
import com.backend.projetointegrador.domain.entities.Role;
import com.backend.projetointegrador.domain.entities.User;
import com.backend.projetointegrador.domain.mappers.UserMapper;
import com.backend.projetointegrador.repositories.UserRepository;
import com.backend.projetointegrador.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final RoleService roleService;

    public List<UserResponseDTO> findAll() {
        //TODO add pagination
        return userRepository.findAll().stream().map(user -> UserMapper.toResponseDTO(user)).toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = findEntityById(id);
        return UserMapper.toResponseDTO(user);
    }

    public UserResponseDTO create(UserRequestDTO dto, String authority) {
        Role role = roleService.findByAuthority(authority);
        //TODO add crypt to password
        User user = new User(null,
                dto.email(),
                dto.password(),
                role);

        return UserMapper.toResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = findEntityById(id);

        user.setEmail(dto.email());
        //TODO add crypt to password
        user.setPassword(dto.password());

        return UserMapper.toResponseDTO(userRepository.save(user));
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(User.class, id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "email: " + email));
    }

}
