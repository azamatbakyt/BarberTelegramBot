package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.Timeslots;
import kz.azamatbakyt.BarberTelegramBot.exception.TimeslotNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.TimeslotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TimeslotsService {

    private TimeslotsRepository timeslotsRepository;

    @Autowired
    public TimeslotsService(TimeslotsRepository timeslotsRepository) {
        this.timeslotsRepository = timeslotsRepository;
    }

    public List<Timeslots> getAll(){
        return timeslotsRepository.findAll();
    }



    public void saveTimeslot(Timeslots timeslot){

        timeslotsRepository.save(timeslot);
    }

    public Timeslots getTimeslot(Long id){
        return timeslotsRepository.findById(id)
                .orElseThrow(() -> new TimeslotNotFoundException("Время не найдено"));
    }

    public void deleteTimeslot(Long id){
        timeslotsRepository.deleteById(id);
    }
}
