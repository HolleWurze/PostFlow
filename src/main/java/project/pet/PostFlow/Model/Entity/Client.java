package project.pet.PostFlow.Model.Entity;

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
import project.pet.PostFlow.Enum.CRUDStatus;
import project.pet.PostFlow.Enum.ClientPriority;

@Getter
@Setter
@Entity
@Table(name = "clients")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "client_priority")
    private ClientPriority clientPriority;

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
    private Department department;

    @OneToMany
    List<Parcel> parcels;

    @OneToMany // каскадное нам впринципе не нужно так как клиента мы не удалим из базы?
    private List<Request> requests;

}
