package de.structuremade.ms.gradeservice.api.json;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditGrade {
    @NotNull
    private String grade;
}
