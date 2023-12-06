package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.*;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomScheduleRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Timeslot> getAvailableTimeslots(
            LocalDate date,
            CustomerService service
    ) {
        final var allTimeslots = getTimeslotsOnDate(date);
        final var bookedTimeslots = getBookedTimeslotsOnDate(date);

        final var availableTimeslots = allTimeslots.stream()
                .filter(t -> !bookedTimeslots.contains(t))
                .collect(Collectors.toList());

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
            return scheduleRepository.findScheduleByDayOfWeek( String.valueOf(date.getDayOfWeek().getValue()))
                    .stream().map(Schedule::getTimeslot)
                    .collect(Collectors.toList());
        } else {
            return timeslots;
        }
    }

    public void createAppointment(
            CustomerService service,
            LocalDate date,
            Client client)
    {
        appointmentRepository.save(new Appointment(client, service, date));
    }

    public List<Appointment> getAll(){
        return appointmentRepository.findAll();
    }

    public Appointment getAppointment(Long id){
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись не найдена"));
    }

    public void save(Appointment appointment){
        appointmentRepository.save(appointment);
    }

    public void delete(Long id){
        appointmentRepository.deleteById(id);
    }
}