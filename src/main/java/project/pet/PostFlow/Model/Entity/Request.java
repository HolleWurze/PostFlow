package project.pet.PostFlow.Model.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Enum.Status;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_sender_id", nullable = false)
    private Client sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_recipient_id", nullable = false)
    private Client recipient;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parcel_id", nullable = false)
    private Parcel parcel;

    @Enumerated(EnumType.STRING)
    RequestType requestType;

    @Enumerated(EnumType.STRING)
    Status status;

    private Integer waitingTime;
}
