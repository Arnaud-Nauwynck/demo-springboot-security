package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class SecurityDbConfiguration {

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

//        UserDetails user = User.builder()
//                .username("user1").password("password1").roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin").password("password").roles("USER", "ADMIN")
//                .build();
//        users.createUser(user);
//        users.createUser(admin);

        UserDetails user1;
        if (! users.userExists("user1")) {
            user1 = User.builder()
                .username("user1").password("password1").roles("USER")
                .build();
            users.createUser(user1);
        } else {
            user1 = users.loadUserByUsername("user1");
        }

        UserDetails userAdmin;
        if (! users.userExists("admin")) {
            userAdmin = User.builder()
                    .username("admin").password("password").roles("ADMIN")
                    .build();
            users.createUser(userAdmin);
        } else {
            userAdmin = users.loadUserByUsername("admin");
            boolean updateDebug = false;
            if (updateDebug) {
                userAdmin = User.builder()
                        .username("admin").password("password").roles("ADMIN")
                        .build();
                users.updateUser(userAdmin);
            }
        }

        return users;
    }


}
