package tda.darkarmy.mvpserver.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService{

    private UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userName).orElseThrow();
        return new CustomUser(user);
    }
}
