package project.pet.PostFlow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AppController {

    @GetMapping(path="/")
    public String home(){
        return "home";
    }
    @GetMapping(path="/login.html")
    public String login(){
        return "login";
    }
    @GetMapping(path="/registration.html")
    public String registration(){
        return "registration";
    }
}
