package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.services.EmployeesService;
import com.mykhailotiutiun_projects.onlinediary.data.services.GradesService;
import com.mykhailotiutiun_projects.onlinediary.data.services.StudentsService;
import com.mykhailotiutiun_projects.onlinediary.data.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsersController {

    @Autowired
    UsersService usersService;
    @Autowired
    EmployeesService employeesService;
    @Autowired
    StudentsService studentsService;
    @Autowired
    GradesService gradesService;

    @GetMapping("/users_manager")
    public String mainPage(){
        return "users_manager/main";
    }

    @GetMapping("/users_manager/users")
    public String usersPage(Model model){
        model.addAttribute("users", usersService.getAllUsers());
        return "users_manager/users";
    }

    @PostMapping("/users_manager/users")
    public String usersActions(@RequestParam(name = "userId") long userId, @RequestParam(name = "action") String action, Model model){
        switch (action) {
            case "delete" -> usersService.deleteUser(userId);
            case "verify" -> usersService.checkUser(userId);
            case "addHeadTeacherRole" -> usersService.addRoleToUser(userId, "ROLE_HEAD_TEACHER");
            case "removeHeadTeacherRole" -> usersService.removeRoleToUser(userId, "ROLE_HEAD_TEACHER");
        }
        return "redirect:/users_manager/users";
    }

    @GetMapping("/users_manager/students")
    public String studentsPage(Model model){
        model.addAttribute("students", studentsService.getAllStudents());
        return "users_manager/students";
    }

    @GetMapping("/users_manager/employees")
    public String employeesPage(Model model){
        model.addAttribute("employees", employeesService.getAllEmployees());
        model.addAttribute("grades", gradesService.getAllGrades());
        return "users_manager/employees";
    }

    @PostMapping("/users_manager/employees")
    public String employeesActions(@RequestParam(name = "employeeId") long employeeId,@RequestParam(name = "specialization", required = false) String specialization, @RequestParam(name = "gradeName", required = false) String gradeName, @RequestParam(name = "action") String action, Model model){
        switch (action) {
            case "setSpecialization" -> employeesService.setSpecialization(employeeId, specialization);
            case "addGrade" -> employeesService.addEmployeeGrade(employeeId, gradeName);
            case "removeGrade" -> employeesService.dropEmployeeGrade(employeeId, gradeName);
        }
        return "redirect:/users_manager/employees";
    }
}
