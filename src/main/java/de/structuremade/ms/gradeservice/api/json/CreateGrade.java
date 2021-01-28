package de.structuremade.ms.gradeservice.api.json;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGrade {
    @NotNull
    private String grade;
    @NotNull
    private String student;
    @NotNull
    private String lesson;
}
