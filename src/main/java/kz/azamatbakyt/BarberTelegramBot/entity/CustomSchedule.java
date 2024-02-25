package kz.azamatbakyt.BarberTelegramBot.entity;


import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
@Table(name = "custom_schedule")
public class CustomSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate customDate;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslot timeslot;

    public CustomSchedule() {}

    public CustomSchedule(Long id, LocalDate customDate, Timeslot timeslot) {
        this.id = id;
        this.customDate = customDate;
        this.timeslot = timeslot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCustomDate() {
        return customDate;
    }

    public void setCustomDate(LocalDate customDate) {
        this.customDate = customDate;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }
    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return "CustomSchedules{" +
                "id=" + id +
                ", custom_LocalDate=" + customDate +
                ", timeslot=" + timeslot +
                '}';
    }


}
