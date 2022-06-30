package com.mykhailotiutiun_projects.onlinediary.data.services;


import com.mykhailotiutiun_projects.onlinediary.data.entites.GradeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.GradesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradesService {

    @Autowired
    private GradesRepository repository;




    public GradeEntity getGradeByName(String name) {
        return repository.findByName(name);
    }

    public List<GradeEntity> getAllGrades(){
        return repository.findAll();
    }


    public void createNewGrade(GradeEntity gradeEntity) {
            if (getGradeByName(gradeEntity.getName()) == null){
                repository.save(gradeEntity);
            }
        }

    public void changeGradeTeacher(long id, String teacherId){
        if(repository.findById(id) != null) {
            GradeEntity gradeEntity = repository.findById(id);
            repository.delete(gradeEntity);
            gradeEntity.setGradeTeacherName(teacherId);
            repository.save(gradeEntity);
        }
    }

    public void deleteGrade(long id){
        if(repository.findById(id) != null) {
            repository.delete(repository.findById(id));
        }
    }

}
