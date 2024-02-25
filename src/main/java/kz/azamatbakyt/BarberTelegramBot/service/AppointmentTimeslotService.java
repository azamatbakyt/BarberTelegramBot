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

    public List<AppointmentTimeslot> getAll() {
        return appointmentTimeslotRepository.findAll();
    }

    public void save(List<AppointmentTimeslot> appointmentTimeslotList) {
        appointmentTimeslotRepository.saveAll(appointmentTimeslotList);
    }

    public AppointmentTimeslot getAppointmentTimeslot(Appointment appointment) {
        return appointmentTimeslotRepository.findAppointmentTimeslotByAppointmentId(appointment.getId());
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

    public List<AppointmentTimeslot> getAllActiveBookings(Long chatId) {
        return appointmentTimeslotRepository.findAllByChatId(chatId);
    }

    public AppointmentTimeslot getAllById(Long id) {
        return appointmentTimeslotRepository.findAllById(id);
    }

    public List<AppointmentTimeslot> getAllByIdIn(List<Long> ids) {
        return appointmentTimeslotRepository.findAllByAppointmentIdIn(ids);
    }

}
