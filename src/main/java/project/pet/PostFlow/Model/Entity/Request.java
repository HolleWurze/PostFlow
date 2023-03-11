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
    private Long id;
    @Enumerated(EnumType.STRING)
    RequestType requestType;
    private String appointmentTime;

    private String waitingTime;

    private String estimatedTime;
    @ManyToOne
    private Department department;

    @ManyToOne
    private Parcel parcel;

    @ManyToOne
    private Client client;

    public Request(Client client, RequestType requestType, String appointmentTime) {
        this.client = client;
        this.requestType = requestType;
        this.appointmentTime = appointmentTime;
    }
}
