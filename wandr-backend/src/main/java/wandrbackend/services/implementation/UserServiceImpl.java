package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.User;
import wandrbackend.entity.repository.UserJPARepository;
import wandrbackend.exception.UserNotFoundException;
import wandrbackend.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserJPARepository userRepository;

    @Autowired
    public UserServiceImpl(UserJPARepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Get all users");
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User getUserById(Long userId) {
        logger.info("Get user {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found : " + userId));
    }

    @Override
    public User createUser(User user) {
        logger.info("Create user with email {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User existing = getUserById(userId);
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        logger.info("Update user {}", userId);
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long userId) {
        User existing = getUserById(userId);
        logger.info("Delete user {}", userId);
        userRepository.delete(existing);
    }
}