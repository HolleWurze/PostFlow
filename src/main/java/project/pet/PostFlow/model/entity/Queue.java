package project.pet.PostFlow.model.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "queue")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Boolean priorityClient;

    Integer nextQueueNumber;

    @OneToOne(cascade = CascadeType.ALL)
    Request currentRequest;

    @OneToMany
    List<Request> requests;
}
