package de.mscho.toftws.user.service;

import de.mscho.toftws.user.entity.User;
import de.mscho.toftws.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public Optional<User> getByAuthToken(String authToken) {
        return userRepo.findByAuthToken(authToken);
    }

    public Optional<User> getByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
