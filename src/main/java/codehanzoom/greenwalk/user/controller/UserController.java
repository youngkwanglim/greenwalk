package codehanzoom.greenwalk.user.controller;


import codehanzoom.greenwalk.global.dto.ResponseDto;
import codehanzoom.greenwalk.global.dto.UserJoinDto;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.dto.UserInfoDto;
import codehanzoom.greenwalk.user.repository.UserRepository;
import codehanzoom.greenwalk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping({"","/"})
    public String index()  {
        return "<h1>index</h1>";
    }

    // ROLE_USER의 회원가입 경로
    @PostMapping("/auth/join")
    public ResponseDto<String> join(@RequestBody UserJoinDto userJoinDto) throws Exception {
        userService.join(userJoinDto);
        return new ResponseDto<String>(HttpStatus.OK.value(), "회원가입 완료");
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoDto> userinfo(){

        Long id = userService.getUserId();
        Optional<User> myUser = userRepository.findById(id);

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(myUser.get().getId())
                .name(myUser.get().getName())
                .email(myUser.get().getEmail())
                .totalDonation(myUser.get().getTotalDonation())
                .totalWalkingDistance(myUser.get().getTotalWalkingDistance())
                .totalPoint(myUser.get().getTotalPoint())
                .totalStep(myUser.get().getTotalStep())
                .totalTrashCount(myUser.get().getTotalTrashCount())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);

    }
}
