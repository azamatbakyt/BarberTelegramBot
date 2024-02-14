package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public List<LocalDate> getDays(){
        List<LocalDate> days = new ArrayList<>();
        LocalDate currendDate = LocalDate.now();
        for (int i = 0; i < 14; i++) {
            days.add(currendDate.plusDays(i));
        }



        return days;
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