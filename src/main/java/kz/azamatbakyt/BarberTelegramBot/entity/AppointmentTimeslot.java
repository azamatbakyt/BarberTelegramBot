package kz.azamatbakyt.BarberTelegramBot.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "appointment_timeslot")
public class AppointmentTimeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;


    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslot timeslot;

    public AppointmentTimeslot() {
    }

    public AppointmentTimeslot(Long id, Appointment appointment, Timeslot timeslot) {
        this.id = id;
        this.appointment = appointment;
        this.timeslot = timeslot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return "AppointmentTimeslot{" +
                "id=" + id +
                ", appointment=" + appointment +
                ", timeslot=" + timeslot +
                '}';
    }
}
