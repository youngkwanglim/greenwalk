package codehanzoom.greenwalk.user.service;


import codehanzoom.greenwalk.global.config.BCryptPasswordEncoderConfig;
import codehanzoom.greenwalk.global.dto.UserJoinDto;
import codehanzoom.greenwalk.user.domain.Role;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoderConfig bCryptPasswordEncoderConfig;

    @Transactional
    // 회원가입 기능(ROLE = USER)
    public void join(UserJoinDto userJoinDto) {

        User user = User.builder()
                .email(userJoinDto.getEmail())
                .password(userJoinDto.getPassword())
                .name(userJoinDto.getName())
                .role(Role.USER)
                .build();

        this.checkEmailDuplicate(user.getEmail());
        //Spring Security 이용 시, 비밀번호 암호화가 필수 조건
        //암호화 후 save() 메서드로 저장
        user.passwordEncode(bCryptPasswordEncoderConfig.bCryptPasswordEncoder());
        userRepository.save(user);
    }

    public void checkEmailDuplicate(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.debug("UserServiceImpl.checkEmailDuplicate exception : {}", email);
            throw new DuplicateKeyException("이미 존재하는 이메일입니다");
        }
    }

    public long getUserId(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user =userRepository.findByEmail(userDetails.getUsername());
        if(user.isEmpty()){
           throw new NoSuchElementException("해당 유저가 존재하지 않습니다.");
        }
        return user.get().getId();
    }

}
