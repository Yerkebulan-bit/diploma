package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.UserType;
import kz.iitu.diploma_resource_server.register.AuthRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthRegister authRegister;

    @Autowired
    public AuthController(AuthRegister authRegister) {
        this.authRegister = authRegister;
    }

    @GetMapping("/identify-user-type")
    public UserType identifyUserType() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        return authRegister.identifyUserType(auth.getName());
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("codeId") String codeId, @RequestParam("code") String code) {
        return authRegister.verifyCode(codeId, code);
    }
}
