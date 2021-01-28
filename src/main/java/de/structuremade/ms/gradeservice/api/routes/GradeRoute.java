package de.structuremade.ms.gradeservice.api.routes;

import com.google.gson.Gson;
import de.structuremade.ms.gradeservice.api.json.CreateGrade;
import de.structuremade.ms.gradeservice.api.json.EditGrade;
import de.structuremade.ms.gradeservice.api.json.answer.GetGrades;
import de.structuremade.ms.gradeservice.api.json.answer.GetGradesOfLesson;
import de.structuremade.ms.gradeservice.api.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/service/grade")
public class GradeRoute {

    @Autowired
    GradeService service;

    Gson gson;

    @CrossOrigin
    @PostMapping("/create")
    public void create(@RequestBody CreateGrade cg, HttpServletResponse response, HttpServletRequest request) {
        switch (service.create(cg, request.getHeader("Authorization").substring(7))) {
            case 0 -> response.setStatus(HttpStatus.CREATED.value());
            case 1 -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            case 2 -> response.setStatus(HttpStatus.BAD_REQUEST.value());
            case 3 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @PutMapping("/edit/{gradeid}")
    public void edit(@RequestBody EditGrade eg, @PathVariable String gradeid, HttpServletRequest request, HttpServletResponse response) {
        switch (service.edit(eg, gradeid, request.getHeader("Authorization").substring(7))) {
            case 0 -> response.setStatus(HttpStatus.OK.value());
            case 1 -> response.setStatus(HttpStatus.BAD_REQUEST.value());
            case 2 -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            case 3 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @GetMapping("/get")
    public Object get(HttpServletResponse response, HttpServletRequest request) {
        GetGrades grades = service.get(request.getHeader("Authorization").substring(7));
        if (grades != null) {
            response.setStatus(HttpStatus.OK.value());
            return gson.toJson(grades);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return null;
    }

    @CrossOrigin
    @GetMapping("/getByLesson/{lessonid}")
    public Object getByLesson(@PathVariable String lessonid, HttpServletResponse response, HttpServletRequest request) {
        GetGradesOfLesson grades = service.getByLesson(lessonid,request.getHeader("Authorization").substring(7));
        if (grades != null) {
            response.setStatus(HttpStatus.OK.value());
            return gson.toJson(grades);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return null;
    }

    @CrossOrigin
    @GetMapping("/getByStudent/{userid}")
    public Object getLessons(@PathVariable String userid, HttpServletResponse response, HttpServletRequest request) {
        GetGrades grades = service.getByUser(userid,request.getHeader("Authorization").substring(7));
        if (grades != null) {
            response.setStatus(HttpStatus.OK.value());
            return gson.toJson(grades);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return null;
    }

}
