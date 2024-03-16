package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedule;
import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomScheduleRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final CustomScheduleRepository customScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public ScheduleService(CustomScheduleRepository customScheduleRepository, ScheduleRepository scheduleRepository, AppointmentService appointmentService) {
        this.customScheduleRepository = customScheduleRepository;
        this.scheduleRepository = scheduleRepository;
        this.appointmentService = appointmentService;
    }



    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    public List<LocalDate> getDays(){
        List<LocalDate> nonDayOffDates = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        // Вы можете заменить этот цикл вашим собственным механизмом генерации дат на 14 дней вперед, если это необходимо
        for (int i = 0; i < 14; i++) {
            LocalDate day = currentDate.plusDays(i);
            List<CustomSchedule> customSchedules = customScheduleRepository.findCustomScheduleByCustomDate(day);
            List<Timeslot> timeslots = appointmentService.getTimeslotsForNonAvailableDay(day);
            boolean isDayOff = false;
            for (CustomSchedule schedule : customSchedules ) {
                if (schedule.getDayOff() || timeslots.isEmpty()) {
                    isDayOff = true;
                    break;
                }
            }
            if (!isDayOff) {
                nonDayOffDates.add(day);
            }
        }

        return nonDayOffDates
                .stream()
                .filter(day -> {
                    if (LocalDate.now().equals(day)){
                        return LocalTime.now().isBefore(LocalTime.of(21, 0, 0));
                    }

                    return true;
                }
                ).collect(Collectors.toList());
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