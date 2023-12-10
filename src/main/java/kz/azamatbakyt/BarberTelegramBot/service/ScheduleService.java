package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }



    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    public List<DayOfWeek> getDaysOfWeek(){
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        LocalDate currendDate = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            daysOfWeek.add(currendDate.getDayOfWeek().plus(i));
        }

        return daysOfWeek;
    }

    public void saveSchedule(Schedule schedule){
        scheduleRepository.save(schedule);
    }

    public Schedule getSchedule(Long id){
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Расписание не найдено"));
    }

    public void deleteSchedule(Long id){
        scheduleRepository.deleteById(id);
    }

}