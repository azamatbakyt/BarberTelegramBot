package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
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

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslot timeslot;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBooking;

    public Appointment() {}

    public Appointment(Long id, Client client, CustomerService service, Timeslot timeslot, LocalDate dateOfBooking) {
        this.id = id;
        this.client = client;
        this.service = service;
        this.timeslot = timeslot;
        this.dateOfBooking = dateOfBooking;
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

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public LocalDate getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(LocalDate dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    @Override
    public String toString() {
        return "Appointments{" +
                "id=" + id +
                ", client=" + client +
                ", service=" + service +
                ", timeslot=" + timeslot +
                ", date_of_booking=" + dateOfBooking +
                '}';
    }
}
