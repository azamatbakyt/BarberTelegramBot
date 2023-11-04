package kz.azamatbakyt.BarberTelegramBot.controller;


import jakarta.ws.rs.Path;
import kz.azamatbakyt.BarberTelegramBot.entity.Schedules;
import kz.azamatbakyt.BarberTelegramBot.service.SchedulesService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedules")
public class SchedulesController {

    private final SchedulesService schedulesService;
    private final TimeslotsService timeslotsService;

    @Autowired
    public SchedulesController(SchedulesService schedulesService, TimeslotsService timeslotsService) {
        this.schedulesService = schedulesService;
        this.timeslotsService = timeslotsService;
    }

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("schedules", schedulesService.getSchedules());

        return "customerService/schedulesList";
    }

    @GetMapping("/new")
    public String getScheduleFrom(Model model){
        model.addAttribute("newSchedule", new Schedules());
        model.addAttribute("timeslots", timeslotsService.getAll());
        return "customerService/schedulesForm";
    }

    @PostMapping("/save")
    public String createSchedule(@ModelAttribute Schedules schedule){
        schedulesService.saveSchedule(schedule);

        return "redirect:/schedules";
    }

    @GetMapping("/{id}/edit")
    public String editSchedule(@PathVariable("id") Long id, Model model){
        model.addAttribute("scheduleToUpdate", schedulesService.getSchedule(id));
        model.addAttribute("timeslots", timeslotsService.getAll());
        return "customerService/schedulesEdit";
    }

    @PostMapping("{id}")
    public String updateSchedule(@PathVariable("id") Long id,
                                 @ModelAttribute("scheduleToUpdate") Schedules schedules){

        Schedules scheduleToUpdate = schedulesService.getSchedule(id);
        if (scheduleToUpdate != null){
            scheduleToUpdate.setDay_of_week(schedules.getDay_of_week());
            scheduleToUpdate.setTimeslot(schedules.getTimeslot());

            schedulesService.saveSchedule(scheduleToUpdate);
        }

        return "redirect:/schedules";
    }

    @GetMapping("/{id}/delete")
    public String deleteSchedule(@PathVariable("id") Long id){
        schedulesService.deleteSchedule(id);

        return "redirect:/schedules";
    }
}
