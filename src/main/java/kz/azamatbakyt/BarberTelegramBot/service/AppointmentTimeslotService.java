package kz.azamatbakyt.BarberTelegramBot.service;

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

    public List<AppointmentTimeslot> getAll(){
        return appointmentTimeslotRepository.findAll();
    }

    public void save(List<AppointmentTimeslot> appointmentTimeslotList) {
        appointmentTimeslotRepository.saveAll(appointmentTimeslotList);
    }

    public AppointmentTimeslot getAppointmentTimeslot(Appointment appointment) {
        return appointmentTimeslotRepository.findAppointmentTimeslotByAppointmentId(appointment.getId());
    }



    public List<AppointmentTimeslot> getListAT(Long id){
        return appointmentTimeslotRepository.findAllByAppointmentId(id);
    }


    public void update(List<Timeslot> timeslots, Appointment appointment) {
        List<AppointmentTimeslot> appointmentTimeslotList = getListAT(appointment.getId());
        for (int i = 0; i < timeslots.size(); i++) {
            appointmentTimeslotList.get(i).setTimeslot(timeslots.get(i));
            appointmentTimeslotList.get(i).setAppointment(appointment);
        }
        appointmentTimeslotRepository.saveAll(appointmentTimeslotList);

    }


}
