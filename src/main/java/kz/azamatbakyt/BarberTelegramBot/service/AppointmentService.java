package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.*;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomScheduleRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentTimeslotRepository appointmentTimeslotRepository;
    private final CustomScheduleRepository customScheduleRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              AppointmentTimeslotRepository appointmentTimeslotRepository,
                              CustomScheduleRepository customScheduleRepository,
                              ScheduleRepository scheduleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentTimeslotRepository = appointmentTimeslotRepository;
        this.customScheduleRepository = customScheduleRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // TODO учитывать duration
    //        t - array of timeslots
    //        s - step
    //        d - duration of service
    //        s =  RoundUp(duration / 60) - 1
    //        diff = s - 1
    //        result = arr[]
    //        for (int i = t.length - 1; i > s + 1;  i--) {
    //            if (t[i].start - t[i-s].end == diff) {
    //                if (t[i].end - t[i - s].start >= d) {
    //                    result.add(t[i])
    //                }
    //            }
    //        }
    public List<Timeslot> getAvailableTimeslots(
            LocalDate date,
            CustomerService service
    ) {
        final var allTimeslots = getTimeslotsOnDate(date);
        final var bookedTimeslots = getBookedTimeslotsOnDate(date);

        final var availableTimeslots = allTimeslots.stream()
                .filter(t -> !bookedTimeslots.contains(t))
                .collect(Collectors.toList());

        final int duration = service.getDuration();
        if (duration > 70) {
            List<Timeslot> timeslotsFor2hourDuration = new ArrayList<>();
            final int s = Math.round((float) duration / 60);
            final int diff = s - 1;
            for (int i = 0; i < availableTimeslots.size() - 1; i++) {
                if (availableTimeslots.get(i + 1).getStartTime().equals( availableTimeslots.get(i).getEndTime()) ||
                        (availableTimeslots.get(i + 1).getStartTime().getHour() - 1 == availableTimeslots.get(i).getEndTime().getHour())) {
                    if ((availableTimeslots.get(i + 1).getEndTime().getHour() - availableTimeslots.get(i).getStartTime().getHour() == s) ||
                            (availableTimeslots.get(i + 1).getEndTime().getHour() - availableTimeslots.get(i).getStartTime().getHour()) == 3) {
                            timeslotsFor2hourDuration.add(availableTimeslots.get(i));
                    }

                }
            }

            return timeslotsFor2hourDuration;
        } else
            return availableTimeslots;
    }

    public List<Timeslot> getBookedTimeslotsOnDate(LocalDate date) {
        final var appointmentIds = appointmentRepository.findAllByDateOfBooking(date)
                .stream()
                .map(Appointment::getId)
                .collect(Collectors.toList());
        return appointmentTimeslotRepository.findAllByAppointmentIdIn(appointmentIds)
                .stream()
                .map(AppointmentTimeslot::getTimeslot)
                .collect(Collectors.toList());
    }

    private List<Timeslot> getTimeslotsOnDate(LocalDate date) {
        final var timeslots = customScheduleRepository.findCustomScheduleByCustomDate(date)
                .stream().map(CustomSchedule::getTimeslot)
                .collect(Collectors.toList());
        if (timeslots.isEmpty()) {
            return scheduleRepository.findScheduleByDayOfWeek(String.valueOf(date.getDayOfWeek().getValue()))
                    .stream().map(Schedule::getTimeslot)
                    .collect(Collectors.toList());
        } else {
            return timeslots;
        }
    }

    public void createAppointment(
            CustomerService service,
            LocalDate date,
            Client client) {
        appointmentRepository.save(new Appointment(client, service, date));
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись не найдена"));
    }

    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }
}