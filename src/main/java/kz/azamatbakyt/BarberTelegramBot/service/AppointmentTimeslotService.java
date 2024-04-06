package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentTimeslotService {

    private final AppointmentTimeslotRepository appointmentTimeslotRepository;

    @Autowired
    public AppointmentTimeslotService(AppointmentTimeslotRepository appointmentTimeslotRepository) {
        this.appointmentTimeslotRepository = appointmentTimeslotRepository;
    }

    public List<AppointmentTimeslot> getAll() {
        return appointmentTimeslotRepository.findAll();
    }

    public List<AppointmentTimeslot> getAllSuccessfulAppointments(Status status){
        return appointmentTimeslotRepository.findAllSuccessfulAppointments(status.toString());
    }

    public void save(List<AppointmentTimeslot> appointmentTimeslotList) {
        appointmentTimeslotRepository.saveAll(appointmentTimeslotList);
    }



    public List<AppointmentTimeslot> getListAT(Long id) {
        return appointmentTimeslotRepository.findAllByAppointmentId(id);
    }


    public void update(List<Timeslot> timeslots, Appointment appointment) {
        List<AppointmentTimeslot> appointmentTimeslotList = getListAT(appointment.getId());

        AppointmentTimeslot appointmentTimeslot = appointmentTimeslotList.get(0);
        appointmentTimeslot.setAppointment(appointment);
        appointmentTimeslot.setTimeslot(timeslots.get(0));

        appointmentTimeslotRepository.saveAll(
                List.of(
                        appointmentTimeslot,
                        new AppointmentTimeslot(appointment, timeslots.get(1))
                )
        );


    }

    public void updateOneHourAppointmnent(List<Timeslot> timeslots, Appointment appointment) {
        List<AppointmentTimeslot> appointmentTimeslotList = getListAT(appointment.getId());

        AppointmentTimeslot appointmentTimeslot = appointmentTimeslotList.get(0);
        appointmentTimeslot.setAppointment(appointment);
        appointmentTimeslot.setTimeslot(timeslots.get(0));

        appointmentTimeslotRepository.save(appointmentTimeslot);


    }

    public AppointmentTimeslot getAllById(Long id) {
        return appointmentTimeslotRepository.findAllById(id);
    }

    public List<AppointmentTimeslot> getAllByIdIn(List<Long> ids) {
        return appointmentTimeslotRepository.findAllByAppointmentIdIn(ids);
    }

    public List<AppointmentTimeslot> getAllByDate(LocalDate date){
        return appointmentTimeslotRepository.findAllByDateOfBooking(date);
    }

    public AppointmentTimeslot getByAppointmentId(Long id){
        return appointmentTimeslotRepository.findByAppointmentId(id);
    }

}
