package project.pet.PostFlow.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.model.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    void deleteByDepartmentId(Long departmentId);
}
