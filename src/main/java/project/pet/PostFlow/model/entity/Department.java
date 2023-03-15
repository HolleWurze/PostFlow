package project.pet.PostFlow.model.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "departments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    String address;

    @OneToMany
    List<Employee> employees;

    @OneToMany
    List<Client> clients;

    @OneToMany
    List<Parcel> parcels;

    @OneToMany
    List<Request> requests;
}
