package project.pet.PostFlow.Model.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "queue")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean priorityClient;

    private Integer nextQueueNumber;

    @OneToOne(cascade = CascadeType.ALL) // при удалении конкретного запроса мы удаляем его
    private Request currentRequest;

    @OneToMany //насчет жадного режима, если currentRequest, то из листа запросов мы его должны удалить
    private List<Request> requests = new ArrayList<>();
}
