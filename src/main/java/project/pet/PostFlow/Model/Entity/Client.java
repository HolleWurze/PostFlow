package project.pet.PostFlow.Model.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.pet.PostFlow.Enum.ClientPriority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(unique = true)
    String uniqueNumber; //нужен ли нам uniqueNumber если есть id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(fetch = FetchType.LAZY)
    List<Parcel> parcels = new ArrayList<>();

    @OneToMany(mappedBy = "client") // каскадное нам впринципе не нужно так как клиента мы не удалим из базы?
    private List<Request> requests = new ArrayList<>();

}
