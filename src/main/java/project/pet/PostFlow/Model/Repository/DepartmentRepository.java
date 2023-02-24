package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.Model.Entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
