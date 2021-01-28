package de.structuremade.ms.gradeservice.utils.database.repo;

import de.structuremade.ms.gradeservice.utils.database.entity.LessonRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRoleRepo extends JpaRepository<LessonRoles, String> {

}
