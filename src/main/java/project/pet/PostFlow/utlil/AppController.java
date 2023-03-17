package project.pet.PostFlow.utlil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    @GetMapping(path="/")
    public String home(Model model){
        return "home";
    }

    @GetMapping(path="/client-login")
    public String clientLogin(Model model){
        return "client-login";
    }

    @GetMapping(path="/registration-client")
    public String registrationClient(Model model){
        return "registration-client";
    }

    @GetMapping(path="/employee-login")
    public String employeeLogin(Model model){
        return "employee-login";
    }
}
