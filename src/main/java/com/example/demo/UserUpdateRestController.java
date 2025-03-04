package com.example.demo;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserUpdateRestController {

    private final UserDetailsManager userDetailsManager;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String ENCODE_PREFIX = "{bcrypt}"; // cf PasswordEncoderFactories

    public UserUpdateRestController(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
        // for debug (uncomment to execute once, or use curl
        updatePassword(new UserPasswordRequestDTO("user2", "password2"));
    }

    record UserPasswordRequestDTO(String username, String newPassword) {}

    /**
     * update password, using for example
     * <PRE>
     *     curl -k -u user1:password1 -H 'content-type:application/json'
     *       -X POST https://localhost:8443/api/v1/users/update-password
     *       -d '{"username": 'user1", "newPassword": "password1"}'
     * </PRE>
     * @param req
     */
    @PostMapping("/update-password")
    public void updatePassword(@RequestBody UserPasswordRequestDTO req) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(req.username);
        String oldPassword = userDetails.getPassword();

        String hashedPassword = ENCODE_PREFIX + passwordEncoder.encode(req.newPassword);

        System.out.println("updating user password: old hashed '" + oldPassword + "' => new '" + hashedPassword + "'");
        UserDetails updatedUser = User.withUserDetails(userDetails).password(hashedPassword).build();
        userDetailsManager.updateUser(updatedUser);
    }
}
