package project.pet.PostFlow.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping(path="/")
    public String home(){
        return "home";
    }
}
