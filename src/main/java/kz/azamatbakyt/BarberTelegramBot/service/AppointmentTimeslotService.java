package kz.azamatbakyt.BarberTelegramBot.service;

import jakarta.transaction.Transactional;
import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.AppointmentTimeslot;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentTimeslotService {

    private final AppointmentTimeslotRepository appointmentTimeslotRepository;
    private final TimeslotService timeslotService;
    @Autowired
    public AppointmentTimeslotService(AppointmentTimeslotRepository appointmentTimeslotRepository, TimeslotService timeslotService) {
        this.appointmentTimeslotRepository = appointmentTimeslotRepository;
        this.timeslotService = timeslotService;
    }

    @Transactional
    public void save(List<AppointmentTimeslot> appointmentTimeslotList){
        appointmentTimeslotRepository.saveAll(appointmentTimeslotList);
    }

    public AppointmentTimeslot getAppointmentTimeslot(Appointment appointment){
        return appointmentTimeslotRepository.findAppointmentTimeslotByAppointmentId(appointment.getId());
    }

    public void update(Timeslot timeslot, Appointment appointment){
        AppointmentTimeslot appointmentTimeslot = getAppointmentTimeslot(appointment);
        if (appointmentTimeslot != null){
            appointmentTimeslot.setTimeslot(timeslot);
            appointmentTimeslotRepository.save(appointmentTimeslot);
        }
    }



}
