package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private CustomerService service;


    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBooking;
    // TODO Boolean class to use
    private Boolean isCreated;
    public Appointment() {}


    public Appointment(Long id,
                       Client client,
                       CustomerService service,
                       LocalDate dateOfBooking,
                       Boolean isCreated){
        this.id = id;
        this.client = client;
        this.service = service;
        this.dateOfBooking = dateOfBooking;
        this.isCreated = isCreated;
    }

    public Appointment(Client client, CustomerService service, LocalDate dateOfBooking) {
        this.client = client;
        this.service = service;
        this.dateOfBooking = dateOfBooking;
    }

    public Appointment(Client client, CustomerService service) {
        this.client = client;
        this.service = service;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CustomerService getService() {
        return service;
    }



    public void setService(CustomerService service) {
        this.service = service;
    }



    public LocalDate getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(LocalDate dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public Boolean getCreated() {
        return isCreated;
    }

    public void setCreated(Boolean created) {
        isCreated = created;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", client=" + client +
                ", service=" + service +
                ", dateOfBooking=" + dateOfBooking +
                ", isCreated=" + isCreated +
                '}';
    }
}
