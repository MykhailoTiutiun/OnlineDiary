package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.managers.UsersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersManagerController {

    @Autowired
    UsersManager usersManager;

    @GetMapping("/users_manager")
    public String mainPage(){
        return "users_manager/main";
    }

    @GetMapping("/users_manager/users")
    public String mainUsersPage(Model model){
        model.addAttribute("users", usersManager.getAllUsers());
        return "users_manager/users";
    }

    @GetMapping("/users_manager/students")
    public String mainStudentsPage(){
        return "users_manager/students";
    }

    @GetMapping("/users_manager/employees")
    public String mainEmployeesPage(){
        return "users_manager/employees";
    }
}
