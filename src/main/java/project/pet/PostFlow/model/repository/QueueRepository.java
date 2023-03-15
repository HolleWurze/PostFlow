package project.pet.PostFlow.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.model.entity.Queue;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long>{
    Optional<Queue> findTopByOrderByIdDesc();
    Optional<Queue> findById(Long queueId);
}