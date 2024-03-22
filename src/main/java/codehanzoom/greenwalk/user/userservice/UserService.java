package codehanzoom.greenwalk.user.userservice;


import codehanzoom.greenwalk.global.config.BCryptPasswordEncoderConfig;
import codehanzoom.greenwalk.global.dto.UserJoinDto;
import codehanzoom.greenwalk.user.userdomain.Role;
import codehanzoom.greenwalk.user.userdomain.User;
import codehanzoom.greenwalk.user.userrepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoderConfig bCryptPasswordEncoderConfig;

    // 회원가입 기능(ROLE = USER)
    public void join(UserJoinDto userJoinDto) {

        User user = User.builder()
                .email(userJoinDto.getEmail())
                .password(userJoinDto.getPassword())
                .nickname(userJoinDto.getNickname())
                .role(Role.USER)
                .build();

        this.checkEmailDuplicate(user.getEmail());
        //Spring Security 이용 시, 비밀번호 암호화가 필수 조건
        //암호화 후 save() 메서드로 저장
        user.passwordEncode(bCryptPasswordEncoderConfig.bCryptPasswordEncoder());
        userRepository.save(user);
    }

    public Optional<User> checkEmailDuplicate(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.debug("UserServiceImpl.checkEmailDuplicate exception : {}", email);
            throw new DuplicateKeyException("Already Exists email");
        }
        return user;
    }

    public Optional<User> checkNicknameDuplicate(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isPresent()) {
            log.debug("UserServiceImpl.checkEmailDuplicate exception : {}", nickname);
            throw new DuplicateKeyException("Already Exists nickname");
        }
        return user;
    }

}
