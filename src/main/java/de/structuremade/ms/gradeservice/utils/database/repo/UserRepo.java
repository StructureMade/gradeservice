package de.structuremade.ms.gradeservice.utils.database.repo;

import de.structuremade.ms.gradeservice.utils.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
