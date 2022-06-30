package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.GradeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.managers.EmployeesManager;
import com.mykhailotiutiun_projects.onlinediary.data.managers.GradesManager;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.EmployeesRepository;
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
    GradesManager gradesManager;
    @Autowired
    EmployeesManager employeesManager;

    @GetMapping("/grades_manager")
    public String mainPage(Model model){
        model.addAttribute("grades", gradesManager.getAllGrades());
        model.addAttribute("gradeForm", new GradeEntity());
        model.addAttribute("employees", employeesManager.getAllEmployees());
        return "grades_manager/main";
    }

    @PostMapping("/grades_manager/create_grade")
    public String createGrade(@ModelAttribute GradeEntity gradeEntity){
        gradesManager.createNewGrade(gradeEntity);
        return "redirect:/grades_manager";
    }

    @PostMapping("/grades_manager")
    public String actions(@RequestParam(name = "action") String action, @RequestParam(name = "gradeId") long gradeId, @RequestParam(name = "employeeName", required = false) String employeeName, Model model){
        switch (action){
            case ("delete") -> gradesManager.deleteGrade(gradeId);
            case ("setMainTeacher") -> gradesManager.changeGradeTeacher(gradeId, employeeName);
        }

        return "redirect:/grades_manager";
    }
}
