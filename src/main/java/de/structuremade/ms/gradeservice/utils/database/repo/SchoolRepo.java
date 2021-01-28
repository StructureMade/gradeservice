package de.structuremade.ms.gradeservice.utils.database.repo;

import de.structuremade.ms.gradeservice.utils.database.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepo extends JpaRepository<School, String> {
}
