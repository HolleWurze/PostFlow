package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.pet.PostFlow.Model.Entity.Queue;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findTopByOrderByIdDesc();

    Optional<Queue> findById(Long queueId);
}
