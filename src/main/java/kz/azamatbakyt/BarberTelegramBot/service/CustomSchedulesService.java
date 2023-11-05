package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedules;
import kz.azamatbakyt.BarberTelegramBot.exception.CustomScheduleNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomSchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomSchedulesService {

    private final CustomSchedulesRepository customSchedulesRepository;


    @Autowired
    public CustomSchedulesService(CustomSchedulesRepository customSchedulesRepository) {
        this.customSchedulesRepository = customSchedulesRepository;
    }

    public List<CustomSchedules> getAllCustomSchedules(){
        return customSchedulesRepository.findAll();
    }

    public void saveCustomSchedule(CustomSchedules customSchedule){
        customSchedulesRepository.save(customSchedule);
    }

    public CustomSchedules getCustomSchedule(Long id){
        return customSchedulesRepository.findById(id)
                .orElseThrow(() -> new CustomScheduleNotFoundException("Кастомное расписание не найдено"));

    }

    public void deleteCustomSchedule(Long id){
        customSchedulesRepository.deleteById(id);
    }
}
