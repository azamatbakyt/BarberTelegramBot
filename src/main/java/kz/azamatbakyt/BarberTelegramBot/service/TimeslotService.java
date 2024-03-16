package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class TimeslotService {

    private final TimeslotRepository timeslotRepository;
    private final AppointmentService appointmentService;
    private final ScheduleService scheduleService;

    @Autowired
    public TimeslotService(TimeslotRepository timeslotRepository, AppointmentService appointmentService, ScheduleService scheduleService) {
        this.timeslotRepository = timeslotRepository;
        this.appointmentService = appointmentService;
        this.scheduleService = scheduleService;
    }

    public List<Timeslot> getAll(){
        return timeslotRepository.findAll();
    }



    public void saveTimeslot(Timeslot timeslot){

        timeslotRepository.save(timeslot);
    }

    public Timeslot getTimeslot(Long id){
        return timeslotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Время не найдено"));
    }

    public void deleteTimeslot(Long id){
        timeslotRepository.deleteById(id);
    }


    public List<Timeslot> getTimeslots(List<LocalTime> times){
        return timeslotRepository.findAllByStartTimeIn(times);
    }


//    public List<Timeslot> getFreeTimeslots(){
//        final var days = scheduleService.getDays();
//        final List<Timeslot> freeTimeslots = new ArrayList<>();
//        for (int i = 0; i < days.size(); i++) {
//            final var timeslotsByDate = appointmentService.get
//        }
//
//
//        return freeTimeslots;
//    }
}
