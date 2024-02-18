package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.*;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomScheduleRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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

        if (service.getDuration() > Duration.ofHours(1).toMinutes()) {
            final var result = new ArrayList<Timeslot>();
            availableTimeslots.forEach(timeslot -> {
                final var timeslotQty = (int) Math.ceil((double) service.getDuration() / Duration.ofHours(1).toMinutes());
                boolean isOk = true;
                for (int i = 0; i < timeslotQty; i++) {
                    if (isOk) {
                        final LocalTime startTime = timeslot.getStartTime().plusHours(i);
                        final LocalTime endTime = timeslot.getEndTime().plusHours(i);
                        isOk = availableTimeslots.stream()
                                .anyMatch(t -> t.getStartTime().equals(startTime) && t.getEndTime().equals(endTime));
                    }
                }
                if (isOk) {
                    result.trimToSize();
                    result.add(timeslot);
                }
            });
            return result;
        }

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

    public Appointment updateDateOfBookingByChatId(Long chatId, LocalDate newDateOfBook, Status statusToUpdate) {
        Appointment appointmentToUpdate = appointmentRepository.findAllByStatusAndAndClientChatId(chatId, Status.SERVICE_SELECTED.toString());
        appointmentToUpdate.setDateOfBooking(newDateOfBook);
        appointmentToUpdate.setStatus(statusToUpdate.toString());
        return appointmentRepository.save(appointmentToUpdate);
        // Appointment is completed now
    }

    public void updateAppointmnetByStatus(Long chatId, Status statusBefore, Status statusAfter){
        Appointment appointment = appointmentRepository.findAllByStatusAndAndClientChatId(chatId, statusBefore.toString());
        appointment.setStatus(statusAfter.toString());
        appointmentRepository.save(appointment);
    }



    public Appointment getNotCreatedAppointmentByChatId(Long chatId, Status status) {
        return appointmentRepository.findByChatId(chatId, status.toString());
    }

    public void setAppointmentCreated(Long chatId, Status status){
        Appointment appointment = getNotCreatedAppointmentByChatId(chatId, status);
        appointment.setStatus(status.toString());
        appointmentRepository.save(appointment);
    }


    public List<Appointment> getActiveAppointments(Long chatId){
        return appointmentRepository.findAllByCreatedAppointment(chatId);
    }

    public void deleteAppointment(Appointment appointment){
        appointmentRepository.delete(appointment);
    }

    public Appointment getCanceledApppointment(Status status){
        return appointmentRepository.findCanceledAppointmnet(status.toString());
    }
}