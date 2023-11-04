package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.Schedules;
import kz.azamatbakyt.BarberTelegramBot.exception.ScheduleNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.SchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulesService {

    private SchedulesRepository schedulesRepository;

    @Autowired
    public SchedulesService(SchedulesRepository schedulesRepository) {
        this.schedulesRepository = schedulesRepository;
    }

    public List<Schedules> getSchedules() {
        return schedulesRepository.findAll();
    }

    public void saveSchedule(Schedules schedule){
        schedulesRepository.save(schedule);
    }

    public Schedules getSchedule(Long id){
        return schedulesRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Расписание не найдено"));
    }

    public void deleteSchedule(Long id){
        schedulesRepository.deleteById(id);
    }
}
