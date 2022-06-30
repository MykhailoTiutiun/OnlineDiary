package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.managers.StudentsManager;
import com.mykhailotiutiun_projects.onlinediary.data.managers.UsersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsersController {

    @Autowired
    UsersManager usersManager;

    @Autowired
    StudentsManager studentsManager;

    @GetMapping("/users_manager")
    public String mainPage(){
        return "users_manager/main";
    }

    @GetMapping("/users_manager/users")
    public String usersPage(Model model){
        model.addAttribute("users", usersManager.getAllUsers());
        return "users_manager/users";
    }

    @PostMapping("/users_manager/users")
    public String actions(@RequestParam(name = "userId") long userId, @RequestParam(name = "action") String action, Model model){
        switch (action) {
            case "delete" -> usersManager.deleteUser(userId);
            case "verify" -> usersManager.checkUser(userId);
            case "addHeadTeacherRole" -> usersManager.addRoleToUser(userId, "ROLE_HEAD_TEACHER");
            case "removeHeadTeacherRole" -> usersManager.removeRoleToUser(userId, "ROLE_HEAD_TEACHER");
        }
        return "redirect:/users_manager/users";
    }

    @GetMapping("/users_manager/students")
    public String studentsPage(Model model){
        model.addAttribute("students", studentsManager.getAllStudents());
        return "users_manager/students";
    }

    @GetMapping("/users_manager/employees")
    public String employeesPage(){
        return "users_manager/employees";
    }
}
