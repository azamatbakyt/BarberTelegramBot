package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedule;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomScheduleService {

    private final CustomScheduleRepository customScheduleRepository;



    @Autowired
    public CustomScheduleService(CustomScheduleRepository customScheduleRepository) {
        this.customScheduleRepository = customScheduleRepository;
    }

    public List<CustomSchedule> getAllCustomSchedules(){
        return customScheduleRepository.findAll();
    }

    public void saveCustomSchedule(CustomSchedule customSchedule){
        customScheduleRepository.save(customSchedule);
    }

    public CustomSchedule getCustomSchedule(Long id){
        return customScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кастомное расписание не найдено"));

    }

    public void deleteCustomSchedule(Long id){
        customScheduleRepository.deleteById(id);
    }
}