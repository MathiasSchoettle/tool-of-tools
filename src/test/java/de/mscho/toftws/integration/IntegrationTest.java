package de.mscho.toftws.integration;

import de.mscho.toftws.user.entity.User;
import de.mscho.toftws.user.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO is this the way to go? or do we just need one bean for the mysql container and start it in a config?

@SpringBootTest
@Testcontainers
public class IntegrationTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest");

    @Autowired
    UserRepo userRepo;

    @Test
    void test() {
        var user = new User();
        user.password = "password_test";
        user.username = "name_test";
        user.authToken = "token_test";

        userRepo.save(user);

        var user2 = userRepo.findByAuthToken("token_test").get();

        assertEquals("name_test", user2.username);
    }
}
