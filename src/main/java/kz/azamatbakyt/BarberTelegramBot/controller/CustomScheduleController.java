package kz.azamatbakyt.BarberTelegramBot.controller;


import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedule;
import kz.azamatbakyt.BarberTelegramBot.service.CustomScheduleService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/custom-schedules")
public class CustomScheduleController {

    private final CustomScheduleService customScheduleService;
    private final TimeslotService timeslotService;

    @Autowired
    public CustomScheduleController(CustomScheduleService customScheduleService, TimeslotService timeslotService) {
        this.customScheduleService = customScheduleService;
        this.timeslotService = timeslotService;
    }


    @GetMapping
    public String getCustomSchedules(Model model){
        model.addAttribute("customSchedules", customScheduleService.getAllCustomSchedules());
        model.addAttribute("timeslotsForCustomSchedules", timeslotService.getAll());

        return "customSchedules/list";
    }

    @GetMapping("/new")
    public String newCustomSchedule(Model model){
        model.addAttribute("newCustomSchedule", new CustomSchedule());
        model.addAttribute("timeslotsForCustomSchedules", timeslotService.getAll());
        return "customSchedules/form";
    }

    @PostMapping("/save")
    public String createCustomSchedule(@ModelAttribute CustomSchedule customSchedule){
        customScheduleService.saveCustomSchedule(customSchedule);

        return "redirect:/custom-schedules";
    }

    @GetMapping("/{id}/edit")
    public String editCustomSchedule(@PathVariable("id") Long id, Model model){

        model.addAttribute("customScheduleToUpdate", customScheduleService.getCustomSchedule(id));
        model.addAttribute("timeslotsForCustomSchedules", timeslotService.getAll());
        return "customSchedules/edit";
    }

    @PostMapping("{id}")
    public String updateCustomSchedule(@PathVariable("id") Long id,
                                       @ModelAttribute("customScheduleToUpdate") CustomSchedule customSchedule){
        CustomSchedule customScheduleToUpdate = customScheduleService.getCustomSchedule(id);
        if (customScheduleToUpdate != null){
            customScheduleToUpdate.setCustomDate(customSchedule.getCustomDate());
            customScheduleToUpdate.setTimeslot(customSchedule.getTimeslot());

            customScheduleService.saveCustomSchedule(customScheduleToUpdate);
        }

        return "redirect:/custom-schedules";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        customScheduleService.deleteCustomSchedule(id);

        return "redirect:/custom-schedules";
    }
}
