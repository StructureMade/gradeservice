package de.structuremade.ms.gradeservice.utils.database.repo;

import de.structuremade.ms.gradeservice.utils.database.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepo extends JpaRepository<Grade, String> {
}
