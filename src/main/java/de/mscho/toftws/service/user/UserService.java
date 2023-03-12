package de.mscho.toftws.service.user;

import de.mscho.toftws.entity.user.User;
import de.mscho.toftws.repository.user.UserRepo;
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
}
