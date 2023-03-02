package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.pet.PostFlow.Model.Entity.Queue;

public interface QueueRepository extends JpaRepository<Queue, Long> {
}
