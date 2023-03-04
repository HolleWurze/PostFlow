package project.pet.PostFlow.Model.Entity;

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
    private List<Request> requests;

}
