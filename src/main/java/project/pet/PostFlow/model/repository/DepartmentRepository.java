package project.pet.PostFlow.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.model.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
