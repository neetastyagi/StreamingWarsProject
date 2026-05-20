package edu.gatech.streamingwars.user;

import edu.gatech.streamingwars.account.AccountRepository;
import edu.gatech.streamingwars.account.AccountUser;
import edu.gatech.streamingwars.streamingservice.StreamingService;
import edu.gatech.streamingwars.streamingservice.StreamingServiceRepository;
import edu.gatech.streamingwars.studio.Studio;
import edu.gatech.streamingwars.studio.StudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudioRepository studioRepository;
    private final AccountRepository accountRepository;
    private final StreamingServiceRepository streamingServiceRepository;


    @Override
    public AppUser registerUser(AppUser appuser) {

        appuser.setPassword(new BCryptPasswordEncoder().encode(appuser.getPassword()));
        userRepository.save(appuser);

        if (appuser.getRole().getName().equals("studio")) {
            Studio studio = new Studio();
            studio.setAppUser(appuser);
            studioRepository.save(studio);
        }
        if(appuser.getRole().getName().equals("streaming service")) {
            StreamingService streamingService = new StreamingService();
            streamingService.setAppUser(appuser);
            streamingServiceRepository.save(streamingService);
        }
        if (appuser.getRole().getName().equals("account")) {
            AccountUser accountUser = new AccountUser();
            accountUser.setUser(appuser);
            accountRepository.save(accountUser);
        }

        return appuser;
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUserName(userName);
        if (appUser == null) {
            throw new UsernameNotFoundException("User name is incorrect/does not exist");
        }
        return User.builder().username(appUser.getUserName()).
                password(appUser.getPassword()).authorities(appUser.getRole().getName()).build();
    }

}
