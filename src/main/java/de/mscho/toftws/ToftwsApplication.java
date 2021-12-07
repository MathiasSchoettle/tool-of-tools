package de.mscho.toftws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ToftwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToftwsApplication.class, args);
    }

    @RestController
    static class BootController {
        @GetMapping("/{name}")
        public ResponseEntity<String> askQuestion(@PathVariable(value = "name") String name) {
            return ResponseEntity.ok("Hey " + name + ", do you like banana pudding?");
        }
    }

}
