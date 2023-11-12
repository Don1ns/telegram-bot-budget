package me.don1ns.telegrambudgetbot.service;

import me.don1ns.telegrambudgetbot.model.User;
import me.don1ns.telegrambudgetbot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User save(User user){
        return userRepository.save(user);
    }
    public Optional<User> getByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
    public Collection<User> getAll() {
        return userRepository.findAll();
    }
}
