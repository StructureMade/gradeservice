package de.structuremade.ms.gradeservice.api.json.answer;

import de.structuremade.ms.gradeservice.api.json.answer.array.GradeLessonArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetGradesOfLesson {
    private List<GradeLessonArray> grades;
}
