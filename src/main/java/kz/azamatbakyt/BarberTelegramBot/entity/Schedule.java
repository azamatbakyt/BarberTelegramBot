package kz.azamatbakyt.BarberTelegramBot.entity;


import jakarta.persistence.*;
import java.time.DayOfWeek;


@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslot timeslot;


    public Schedule() {
    }

    public Schedule(Long id, DayOfWeek dayOfWeek, Timeslot timeslot) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.timeslot = timeslot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek day_of_week) {
        this.dayOfWeek = day_of_week;
    }



    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return "Schedules{" +
                "id=" + id +
                ", day_of_week='" + dayOfWeek + '\'' +
                ", timeslot=" + timeslot +
                '}';
    }
}
