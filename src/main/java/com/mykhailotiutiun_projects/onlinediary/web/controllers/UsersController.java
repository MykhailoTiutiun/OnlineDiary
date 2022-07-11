package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.StudentEntity;
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

    Long studentId = null;

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
            case "addDirectorRole" -> usersService.addRoleToUser(userId, "ROLE_DIRECTOR");
            case "removeDirectorRole" -> usersService.removeRoleToUser(userId, "ROLE_DIRECTOR");
            case "removeHeadTeacherRole" -> usersService.removeRoleToUser(userId, "ROLE_HEAD_TEACHER");
        }
        return "redirect:/users_manager/users";
    }


    @GetMapping("/users_manager/students")
    public String studentsPage(Model model){
        model.addAttribute("students", studentsService.getAllStudents());
        model.addAttribute("grades", gradesService.getAllGrades());
        return "users_manager/students";
    }

    @PostMapping("/users_manager/students")
    public String studentsActions(@RequestParam(name = "studentId", required = false) Long studentId, @RequestParam(name = "gradeName", required = false) String gradeName, @RequestParam(name = "action") String action, Model model){
        switch (action) {
            case "setGrade" -> studentsService.setGrade(studentId, gradeName);
            case "marksTables" -> {this.studentId = studentId; return "redirect:/users_manager/marks";}
        }
        return "redirect:/users_manager/students";
    }

    @GetMapping("/users_manager/marks")
    public String marksPage(Model model){
        model.addAttribute("studentName", studentsService.getStudentById(studentId).getName());
        model.addAttribute("marksMap", studentsService.getMapByStudentId(studentId, 0));
        model.addAttribute("semesterMarksMap", studentsService.getMapByStudentId(studentId, 1));
        model.addAttribute("yearlyMarksMap", studentsService.getMapByStudentId(studentId, 2));

        return "/users_manager/marks";
    }

    @PostMapping("/users_manager/marks")
    public String marksActions(@RequestParam(name = "action") String action, @RequestParam(name = "lesson") String lesson, @RequestParam(name = "mark", required = false) Integer mark, @RequestParam(name = "typeOfMark") Integer typeOfMark){
        switch (action){
            case ("addMark") -> studentsService.addMarks(studentId, lesson, mark, typeOfMark);
            case ("removeMark") -> studentsService.removeMark(studentId, lesson, typeOfMark);
        }

        return "redirect:/users_manager/marks";
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
