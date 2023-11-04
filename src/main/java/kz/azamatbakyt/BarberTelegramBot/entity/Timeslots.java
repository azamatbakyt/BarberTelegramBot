package kz.azamatbakyt.BarberTelegramBot.entity;


import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "timeslots")
public class Timeslots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIME)
    private LocalTime start_time;

    @Temporal(TemporalType.TIME)
    private LocalTime end_time;

    public Timeslots() {
    }

    public Timeslots(Long id, LocalTime start_time, LocalTime end_time) {
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "Timeslots{" +
                "id=" + id +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}
