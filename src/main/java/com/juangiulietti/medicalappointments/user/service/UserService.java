package com.juangiulietti.medicalappointments.user.service;

import com.juangiulietti.medicalappointments.exception.DuplicateException;
import com.juangiulietti.medicalappointments.security.PasswordGenerator;
import com.juangiulietti.medicalappointments.user.entity.User;
import com.juangiulietti.medicalappointments.user.entity.UserRole;
import com.juangiulietti.medicalappointments.user.repository.UserRepository;
import com.juangiulietti.medicalappointments.user.result.CreatedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordGenerator passwordGenerator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordGenerator = passwordGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CreatedUser createUser(String firstName, String lastName, String email, String phoneNumber, UserRole role){
        String normalizedEmail = email.strip().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(normalizedEmail)){
            throw new DuplicateException("Email " + normalizedEmail + " already exists");
        }

        String initialPassword = passwordGenerator.generate();
        String passwordHash = passwordEncoder.encode(initialPassword);
        User user = new User(firstName, lastName, normalizedEmail, passwordHash, phoneNumber, Set.of(role));
        User savedUser = userRepository.save(user);

        return new CreatedUser(savedUser, initialPassword);
    }
}
