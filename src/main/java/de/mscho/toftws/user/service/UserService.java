package de.mscho.toftws.user.service;

import de.mscho.toftws.user.entity.User;
import de.mscho.toftws.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getByAuthToken(String authToken) {
        return userRepo.findByAuthToken(authToken);
    }

    public Optional<User> getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    public void createUser(String username, String authToken) {
        var user = new User();
        user.username = username;
        user.authToken = authToken;
        userRepo.save(user);
    }

    public void deleteUser(String username) {
        userRepo.deleteUserByUsername(username);
    }
}
