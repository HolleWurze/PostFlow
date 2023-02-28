package project.pet.PostFlow.Model.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.Enum.Status;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "parcels")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    String trackingNumber;

    @Column(columnDefinition = "text")
    String description;

    Double weight;

    @Enumerated(EnumType.STRING)
    Status status;
}
