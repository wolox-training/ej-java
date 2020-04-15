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

    public User updateUser(User user, User editedUser) {
        user.setUserName(editedUser.getUserName());
        user.setName(editedUser.getName());
        user.setBirthDate(editedUser.getBirthDate());

        return userRepository.save(user);
    }
}
