package com.example.ecommerce.service;

import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZipCode(user.getZipCode());
        dto.setCountry(user.getCountry());
        return dto;
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setState(userDTO.getState());
        user.setZipCode(userDTO.getZipCode());
        user.setCountry(userDTO.getCountry());
        
        User savedUser = userRepository.save(user);
        return getUser(savedUser.getId());
    }
}
