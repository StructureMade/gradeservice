package de.structuremade.ms.gradeservice.api.service;

import de.structuremade.ms.gradeservice.api.json.CreateGrade;
import de.structuremade.ms.gradeservice.api.json.EditGrade;
import de.structuremade.ms.gradeservice.api.json.answer.GetGrades;
import de.structuremade.ms.gradeservice.api.json.answer.GetGradesOfLesson;
import de.structuremade.ms.gradeservice.api.json.answer.array.GradeArray;
import de.structuremade.ms.gradeservice.api.json.answer.array.GradeLessonArray;
import de.structuremade.ms.gradeservice.utils.JWTUtil;
import de.structuremade.ms.gradeservice.utils.database.entity.Grade;
import de.structuremade.ms.gradeservice.utils.database.entity.LessonRoles;
import de.structuremade.ms.gradeservice.utils.database.entity.User;
import de.structuremade.ms.gradeservice.utils.database.repo.GradeRepo;
import de.structuremade.ms.gradeservice.utils.database.repo.LessonRoleRepo;
import de.structuremade.ms.gradeservice.utils.database.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeService {

    private final Logger LOGGER = LoggerFactory.getLogger(GradeService.class);

    @Autowired
    GradeRepo gradeRepo;

    @Autowired
    LessonRoleRepo lessonRoleRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JWTUtil jwtUtil;

    @Transactional
    public int create(CreateGrade cg, String jwt) {
        LessonRoles lr;
        User student;
        try {
            LOGGER.info("Init Grade Entity");
            Grade grade = new Grade();
            LOGGER.info("Convert grade to int");
            grade.setGrade(convertGrade(cg.getGrade()));
            if (grade.getGrade() == 7) return 2;
            LOGGER.info("Try to get Lessonrole");
            lr = lessonRoleRepo.getOne(cg.getLesson());
            LOGGER.info("Try to get Student");
            student = userRepo.getOne(cg.getStudent());
            LOGGER.info("Check if User have rights to do this Action");
            if (!lr.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))
                    || !lr.getTeacher().getId().equals(jwtUtil.extractIdOrEmail(jwt))
                    || !student.getSchools().get(0).getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid")))
                return 1;
            grade.setLesson(lr);
            grade.setStudent(student);
            LOGGER.info("All was fine and now grade will be saved");
            gradeRepo.save(grade);
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't create Grade", e.fillInStackTrace());
            return 3;
        }
    }

    @Transactional
    public int edit(EditGrade eg, String gradeId, String jwt) {
        LessonRoles lr;
        int grade;
        try {
            LOGGER.info("Check if grade has right format");
            if ((grade = convertGrade(eg.getGrade())) != 7) return 1;
            Grade gradeEntity = gradeRepo.getOne(gradeId);
            lr = gradeEntity.getLesson();
            LOGGER.info("Check if Teacher have right to edit this Grade");
            if (!lr.getTeacher().getId().equals(jwtUtil.extractIdOrEmail(jwt))) return 2;
            gradeEntity.setGrade(grade);
            gradeRepo.save(gradeEntity);
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't edit grade", e.fillInStackTrace());
            return 3;
        }
    }

    @Transactional
    public GetGrades get(String jwt) {
        List<GradeArray> gradeArrays = new ArrayList<>();
        try {
            LOGGER.info("Get grades of user and add it to List with Lesson");
            userRepo.getOne(jwtUtil.extractIdOrEmail(jwt)).getGrades().forEach(grade -> {
                GradeArray gradeArray = new GradeArray();
                gradeArray.setGrade(grade.getGrade());
                gradeArray.setLesson(grade.getLesson().getName());
                gradeArrays.add(gradeArray);
            });
            LOGGER.info("Got all grades and return it to Client");
            return new GetGrades(gradeArrays);
        } catch (Exception e) {
            LOGGER.error("Couldn't get Grades of User", e.fillInStackTrace());
            return null;
        }
    }

    @Transactional
    public GetGrades getByUser(String user, String jwt) {
        List<GradeArray> gradeArrays = new ArrayList<>();
        try {
            LOGGER.info("Get grades of user and add it to List with Lesson");
            User student = userRepo.getOne(user);
            LOGGER.info("Check if user is from same school like requester");
            if (student.getSchools().get(0).getId().equals(jwtUtil.extractIdOrEmail(jwt))) return new GetGrades();

            student.getGrades().forEach(grade -> {
                GradeArray gradeArray = new GradeArray();
                gradeArray.setGrade(grade.getGrade());
                gradeArray.setLesson(grade.getLesson().getName());
                gradeArrays.add(gradeArray);
            });
            LOGGER.info("Got all grades and return it to Client");
            return new GetGrades(gradeArrays);
        } catch (Exception e) {
            LOGGER.error("Couldn't get Grades of User", e.fillInStackTrace());
            return null;
        }
    }

    @Transactional
    public GetGradesOfLesson getByLesson(String lesson, String jwt) {
        List<GradeLessonArray> grades = new ArrayList<>();
        try {
            LOGGER.info("Get Lesson");
            LessonRoles lr = lessonRoleRepo.getOne(lesson);
            LOGGER.info("Check if User have permissions to do it");
            if (!lr.getTeacher().getId().equals(jwtUtil.extractIdOrEmail(jwt))) return new GetGradesOfLesson();
            LOGGER.info("Get grades of Lesson and add it to List");
            lr.getGrades().forEach(grade -> {
                GradeLessonArray gradeArray = new GradeLessonArray();
                User student = grade.getStudent();
                gradeArray.setGrade(grade.getGrade());
                gradeArray.setStudent(student.getFirstname() + " " + student.getName());
                gradeArray.setStudentId(student.getId());
                grades.add(gradeArray);
            });
            LOGGER.info("Got all grades and return it to Client");
            return new GetGradesOfLesson(grades);
        } catch (Exception e) {
            LOGGER.error("Couldn't get Grades of Lesson", e.fillInStackTrace());
            return null;
        }
    }

    private int convertGrade(String grade) {
        switch (grade) {
            case "sehr gut", "1" -> {
                return 1;
            }
            case "gut", "2" -> {
                return 2;
            }
            case "befriedigend", "3" -> {
                return 3;
            }
            case "ausreichend", "4" -> {
                return 4;
            }
            case "mangelhaft", "5" -> {
                return 5;
            }
            case "ungenügend", "6" -> {
                return 6;
            }
            default -> {
                return 7;
            }
        }
    }

}
