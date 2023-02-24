package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.pet.PostFlow.Model.Entity.Parcel;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    List<Parcel> findByDepartmentId(Long departmentId);
}
