package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.UserEntity;
import com.mykhailotiutiun_projects.onlinediary.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    UsersService usersService;

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userForm", new UserEntity());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute UserEntity userEntity, BindingResult result, Model model){

        if (!userEntity.getPassword().equals(userEntity.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        if(result.hasErrors()){
            return "registration";
        }

        usersService.addUser(userEntity);

        return "redirect:/";
    }

}
