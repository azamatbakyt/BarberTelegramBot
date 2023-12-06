package kz.azamatbakyt.BarberTelegramBot.controller;


import kz.azamatbakyt.BarberTelegramBot.entity.Schedule;
import kz.azamatbakyt.BarberTelegramBot.service.ScheduleService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;


@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TimeslotService timeslotService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, TimeslotService timeslotService) {
        this.scheduleService = scheduleService;
        this.timeslotService = timeslotService;
    }

    //TODO
    @GetMapping
    public String getAll(Model model){
        model.addAttribute("schedulees", scheduleService.getSchedules());

        return "schedules/list";
    }

    @GetMapping("/new")
    public String getScheduleFrom(Model model){
        model.addAttribute("newSchedule", new Schedule());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("timeslots", timeslotService.getAll());
        return "schedules/form";
    }

    @PostMapping("/save")
    public String createSchedule(@ModelAttribute Schedule schedule){
        scheduleService.saveSchedule(schedule);

        return "redirect:/schedules";
    }

    @GetMapping("/{id}/edit")
    public String editSchedule(@PathVariable("id") Long id, Model model){
        model.addAttribute("scheduleToUpdate", scheduleService.getSchedule(id));
        model.addAttribute("timeslots", timeslotService.getAll());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        return "schedules/edit";
    }

    @PostMapping("{id}")
    public String updateSchedule(@PathVariable("id") Long id,
                                 @ModelAttribute("scheduleToUpdate") Schedule schedules){

        Schedule scheduleToUpdate = scheduleService.getSchedule(id);
        if (scheduleToUpdate != null){
            scheduleToUpdate.setDayOfWeek(schedules.getDayOfWeek());
            scheduleToUpdate.setTimeslot(schedules.getTimeslot());

            scheduleService.saveSchedule(scheduleToUpdate);
        }

        return "redirect:/schedules";
    }

    @GetMapping("/{id}/delete")
    public String deleteSchedule(@PathVariable("id") Long id){
        scheduleService.deleteSchedule(id);

        return "redirect:/schedules";
    }
}
