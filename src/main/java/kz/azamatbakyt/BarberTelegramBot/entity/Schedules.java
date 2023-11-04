package kz.azamatbakyt.BarberTelegramBot.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "schedules")
public class Schedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day_of_week;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslots timeslot;


    public Schedules() {
    }

    public Schedules(Long id, String day_of_week, Timeslots timeslot) {
        this.id = id;
        this.day_of_week = day_of_week;
        this.timeslot = timeslot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
    }

    public Timeslots getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslots timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return "Schedules{" +
                "id=" + id +
                ", day_of_week='" + day_of_week + '\'' +
                ", timeslot=" + timeslot +
                '}';
    }
}
