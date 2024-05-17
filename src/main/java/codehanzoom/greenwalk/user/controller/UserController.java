package codehanzoom.greenwalk.user.controller;

import codehanzoom.greenwalk.global.dto.UserJoinDto;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.dto.UserDto;
import codehanzoom.greenwalk.user.repository.UserRepository;
import codehanzoom.greenwalk.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "users", description = "회원 API")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "루트 경로 확인")
    @GetMapping("/")
    public String index()  {
        return "<h1>index</h1>";
    }

    // ROLE_USER의 회원가입 경로
    @Operation(summary = "회원가입 경로")
    @PostMapping("/auth/join")
    public ResponseEntity<String> join(@RequestBody UserJoinDto userJoinDto) throws Exception {
        userService.join(userJoinDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @Operation(summary = "회원정보 가져오기")
    @GetMapping("/userInfo")
    public ResponseEntity<UserDto> userinfo(){

        Long id = userService.getUserId();
        User user = userRepository.findById(id).get();
        UserDto userDto = userService.createUserDto(user);

        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "회원 삭제하기")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("유저가 성공적으로 삭제되었습니다.");
    }
}
