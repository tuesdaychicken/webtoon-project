package com.example.webtoon.user;

import com.example.webtoon.user.dto.ChangePasswordRequest;
import com.example.webtoon.user.dto.CreateUserRequest;
import com.example.webtoon.user.dto.UpdateUserRequest;
import com.example.webtoon.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    /**
     * 유저 생성 요청
     * @param req
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest req){

        Long id = userService.register(
                req.getUsername(),
                req.getName(),
                req.getPassword(),
                req.getEmail(),
                req.getNickname()
        );

        return ResponseEntity.created(URI.create("/api/users/"+req.getUsername())).build();
    }

    /**
     * 유저 조회
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(u -> ResponseEntity.ok(UserResponse.from(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 유저 수정 요청(이름 이메일 닉네임)
     * @param username
     * @param req
     * @return
     */
    @PatchMapping("/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UpdateUserRequest req){
        userService.updateUser(username, req.getName(), req.getEmail(), req.getNickname());
        return ResponseEntity.noContent().build();
    }

    /**
     * 비밀 번호 변경 요청
     * @param username
     * @param req
     * @return
     */
    @PatchMapping("/{username}/password")
    public ResponseEntity<Void> changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest req
    ) {
        userService.changePassword(username, req.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * 유저 삭제 요청
     * @param username
     * @return
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
