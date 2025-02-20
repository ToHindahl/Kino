package de.fhdw.Kino.App;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSenderService userSenderService;

    @Autowired
    public UserController(UserSenderService userSenderService) {
        this.userSenderService = userSenderService;
    }

    @PostMapping
    public String sendUser(@RequestBody UserImpl user) {
        userSenderService.sendUser(user);
        return "User gesendet!";
    }
}