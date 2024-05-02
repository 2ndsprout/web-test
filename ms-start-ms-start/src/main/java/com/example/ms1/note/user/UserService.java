package com.example.ms1.note.user;

import com.example.ms1.note.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void create (String username, String password, String email) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setCreateDate(LocalDateTime.now());

        this.userRepository.save(user);
    }
    public SiteUser getUser (String username) {
        Optional<SiteUser> user = this.userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new DataNotFoundException("user not found");
        }
    }
}
