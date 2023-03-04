package project.pet.PostFlow.Model.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.Enum.RequestType;

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

    @Enumerated(EnumType.STRING)
    RequestType requestType;

//    @Enumerated(EnumType.STRING)
//    Status status;

    private String appointmentTime;

    private String appointmentDateTime;

    private String waitingTime;

    private String EstimatedTime;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parcel_id", nullable = false)
    private Parcel parcel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Request(Client client, RequestType requestType, String appointmentTime) {
        this.client = client;
        this.requestType = requestType;
        this.appointmentTime = appointmentTime;
    }
}
