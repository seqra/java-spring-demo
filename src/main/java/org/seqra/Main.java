package org.seqra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.seqra")
@EntityScan("org.seqra.spring.persistence")
@EnableJpaRepositories("org.seqra.spring.persistence")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
