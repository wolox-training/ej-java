package wolox.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private PasswordEncoderService passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User updatePassword(User user, String password) {
        user.setPassword(password);

        return userRepository.save(user);
    }
}
