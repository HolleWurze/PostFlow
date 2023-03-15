package project.pet.PostFlow.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "parcels")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name = "department_id")
    Department department;

    String trackingNumber;

    @Column(columnDefinition = "text")
    String description;

    Double weight;

    @Enumerated(EnumType.STRING)
    Status status;
}
