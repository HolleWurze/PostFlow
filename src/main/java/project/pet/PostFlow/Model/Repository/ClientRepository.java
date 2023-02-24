package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Department;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByStatus(String status);
    List<Client> findByDepartment(Department department);
    Client findByUniqueNumber(String uniqueNumber);
}
