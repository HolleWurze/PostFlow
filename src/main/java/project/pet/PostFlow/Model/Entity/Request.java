package project.pet.PostFlow.Model.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.Enum.RequestType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
