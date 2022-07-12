package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.StudentEntity;
import com.mykhailotiutiun_projects.onlinediary.services.EmployeesService;
import com.mykhailotiutiun_projects.onlinediary.services.StudentsService;
import com.mykhailotiutiun_projects.onlinediary.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    UsersService usersService;
    @Autowired
    EmployeesService employeesService;
    @Autowired
    StudentsService studentsService;
    @GetMapping("/profile")
    public String profilePage(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("user", usersService.getUserByName(auth.getName()));

        if (!usersService.getUserByName(auth.getName()).isEmployee()) {
            StudentEntity studentEntity = studentsService.getStudentByName(auth.getName());

            model.addAttribute("student", studentEntity);
            model.addAttribute("marksMap", studentsService.getMapByStudentId(studentEntity.getId(), 0));
            model.addAttribute("semesterMarksMap", studentsService.getMapByStudentId(studentEntity.getId(), 1));
            model.addAttribute("yearlyMarksMap", studentsService.getMapByStudentId(studentEntity.getId(), 2));

        } else {
            model.addAttribute("employee", employeesService.getEmployeeByName(auth.getName()));
        }
        return "profile";
    }
}
