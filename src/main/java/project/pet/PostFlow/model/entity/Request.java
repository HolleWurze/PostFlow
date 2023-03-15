package project.pet.PostFlow.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.enums.RequestType;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    RequestType requestType;
    String appointmentTime;

    String waitingTime;

    String estimatedTime;
    @ManyToOne
    Department department;

    @ManyToOne
    Parcel parcel;

    @ManyToOne
    Client client;

    public Request(Client client, RequestType requestType, String appointmentTime) {
        this.client = client;
        this.requestType = requestType;
        this.appointmentTime = appointmentTime;
    }
}
