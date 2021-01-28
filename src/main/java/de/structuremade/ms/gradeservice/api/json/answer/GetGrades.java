package de.structuremade.ms.gradeservice.api.json.answer;

import de.structuremade.ms.gradeservice.api.json.answer.array.GradeArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGrades {
    List<GradeArray> grades;
}
