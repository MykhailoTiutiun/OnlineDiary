package com.mykhailotiutiun_projects.onlinediary.web.controllers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.managers.GradesManager;
import com.mykhailotiutiun_projects.onlinediary.data.managers.LessonsTypesManager;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.LessonsTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LessonsController {

    @Autowired
    LessonsTypesManager lessonsTypesManager;
    @Autowired
    GradesManager gradesManager;
    @Autowired
    LessonsTypesRepository lessonsTypesRepository;


    @GetMapping("/lessons_manager")
    public String mainPage(Model model){
        model.addAttribute("lessons", lessonsTypesManager.getAllLessonType());
        model.addAttribute("grades", gradesManager.getAllGrades());
        model.addAttribute("lessonForm", new LessonTypeEntity());
        return "lessons_manager/main";
    }

    @PostMapping("/lessons_manager/create")
    public String createLesson(@ModelAttribute LessonTypeEntity lessonTypeEntity, Model model){
        lessonsTypesRepository.save(lessonTypeEntity);
        return "redirect:/lessons_manager";
    }

    @PostMapping("/lessons_manager")
    public String actions(@RequestParam(name = "action") String action, @RequestParam(name = "lessonId") long lessonId, @RequestParam(name = "gradeName", required = false) String gradeName, Model model){
        switch(action){
            case ("addGrade") -> lessonsTypesManager.addLessonTypeGrade(lessonId, gradeName);
            case ("removeGrade") -> lessonsTypesManager.dropLessonTypeGrade(lessonId, gradeName);
            case ("delete") -> lessonsTypesManager.deleteLessonType(lessonId);
        }
        return "redirect:/lessons_manager";
    }
}
