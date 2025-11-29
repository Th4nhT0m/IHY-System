package com.ihy.app.common.config;

import com.ihy.app.common.constant.Role;
import com.ihy.app.entity.Users;
import com.ihy.app.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {


    PasswordEncoder passwordEncoder;

    /**
     *
     * @param repository
     * @return
     */
    @Bean
    ApplicationRunner applicationRunner(UserRepository repository){
        return args -> {
            if(repository.findUsersByEmail("admin@testmail.com").isEmpty()){
                HashSet roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                Users usersAdmin = Users.builder()
                        .email("admin@testmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .name("admin")
                        .roles(roles)
                        .isActive(1)
                        .build();

                repository.save(usersAdmin);

                log.warn("Admin user has been default (mail: admin@testmail.com ,password:admin). Please change password next time");
            }
        };
    }

}
