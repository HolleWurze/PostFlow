package project.pet.PostFlow.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import project.pet.PostFlow.enums.CRUDStatus;
import project.pet.PostFlow.enums.ClientPriority;

@Entity
@Table(name = "clients")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "client_priority")
    ClientPriority clientPriority;

    @Column(name = "user_name")
    String userName;
    @Column(name = "password")
    String password;
    @Column(name = "email")
    String email;

    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdAt;

    @Column(name = "updated_date")
    LocalDateTime updatedAt;

    @Column(name = "deleted_date")
    LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    CRUDStatus status = CRUDStatus.CREATED;

    @ManyToOne
    @JoinColumn(name = "department_id")
    Department department;

    @OneToMany
    List<Parcel> parcels;

    @OneToMany
    List<Request> requests;
}
