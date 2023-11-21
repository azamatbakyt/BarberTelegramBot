package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
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
