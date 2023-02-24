package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.Model.Entity.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByDepartmentId(Long departmentId);

    void deleteByDepartmentId(Long departmentId);
}
