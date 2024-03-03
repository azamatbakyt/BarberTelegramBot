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

    private Boolean dayOff;


    public CustomSchedule() {}

    public CustomSchedule(Long id, LocalDate customDate, Timeslot timeslot, Boolean dayOff) {
        this.id = id;
        this.customDate = customDate;
        this.timeslot = timeslot;
        this.dayOff = dayOff;
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

    public Boolean getDayOff() {
        return dayOff;
    }

    public void setDayOff(Boolean dayOff) {
        this.dayOff = dayOff;
    }

    @Override
    public String toString() {
        return "CustomSchedule{" +
                "id=" + id +
                ", customDate=" + customDate +
                ", timeslot=" + timeslot +
                ", dayOff=" + dayOff +
                '}';
    }
}
