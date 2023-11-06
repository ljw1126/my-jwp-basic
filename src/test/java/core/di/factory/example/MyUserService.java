package core.di.factory.example;

import core.annotation.Inject;
import core.annotation.Service;

@Service
public class MyUserService {
    @Inject
    private UserRepository userRepository;

    public MyUserService() {
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
