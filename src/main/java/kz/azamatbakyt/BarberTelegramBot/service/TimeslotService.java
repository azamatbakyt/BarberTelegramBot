package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class TimeslotService {

    private final TimeslotRepository timeslotRepository;

    @Autowired
    public TimeslotService(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
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

    public Timeslot getTimeslotByStartTime(LocalTime startTime){
        return timeslotRepository.findTimeslotByStartTime(startTime);
    }

    public List<Timeslot> getTimeslots(List<LocalTime> times){
        return timeslotRepository.findAllByStartTimeIn(times);
    }

    public List<Timeslot> getAllActiveTimeslotByChatId(Long chatId){
        return timeslotRepository.findAllActiveTimeslots(chatId);
    }
}
