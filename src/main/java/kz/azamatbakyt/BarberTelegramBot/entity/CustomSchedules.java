package kz.azamatbakyt.BarberTelegramBot.entity;


import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "custom_schedules")
public class CustomSchedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date custom_date;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslots timeslot;

    public CustomSchedules() {}

    public CustomSchedules(Long id, Date custom_date, Timeslots timeslot) {
        this.id = id;
        this.custom_date = custom_date;
        this.timeslot = timeslot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCustom_date() {
        return custom_date;
    }

    public void setCustom_date(Date custom_date) {
        this.custom_date = custom_date;
    }

    public Timeslots getTimeslot() {
        return timeslot;
    }

    @Override
    public String toString() {
        return "CustomSchedules{" +
                "id=" + id +
                ", custom_date=" + custom_date +
                ", timeslot=" + timeslot +
                '}';
    }

    public void setTimeslot(Timeslots timeslot) {
        this.timeslot = timeslot;
    }
}
