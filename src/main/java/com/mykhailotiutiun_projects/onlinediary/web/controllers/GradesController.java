package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.GradeEntity;
import com.mykhailotiutiun_projects.onlinediary.services.EmployeesService;
import com.mykhailotiutiun_projects.onlinediary.services.GradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GradesController {

    @Autowired
    GradesService gradesService;
    @Autowired
    EmployeesService employeesService;

    @GetMapping("/grades_manager")
    public String mainPage(Model model){
        model.addAttribute("grades", gradesService.getAllGrades());
        model.addAttribute("gradeForm", new GradeEntity());
        model.addAttribute("employees", employeesService.getAllEmployees());
        return "grades_manager/main";
    }

    @PostMapping("/grades_manager/create_grade")
    public String createGrade(@ModelAttribute GradeEntity gradeEntity){
        gradesService.createNewGrade(gradeEntity);
        return "redirect:/grades_manager";
    }

    @PostMapping("/grades_manager")
    public String actions(@RequestParam(name = "action") String action, @RequestParam(name = "gradeId") long gradeId, @RequestParam(name = "employeeName", required = false) String employeeName, Model model){
        switch (action){
            case ("delete") -> gradesService.deleteGrade(gradeId);
            case ("setMainTeacher") -> gradesService.changeGradeTeacher(gradeId, employeeName);
        }

        return "redirect:/grades_manager";
    }
}
