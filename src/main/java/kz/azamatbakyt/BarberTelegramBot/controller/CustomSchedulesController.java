package kz.azamatbakyt.BarberTelegramBot.controller;


import kz.azamatbakyt.BarberTelegramBot.entity.CustomSchedules;
import kz.azamatbakyt.BarberTelegramBot.service.CustomSchedulesService;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/custom-schedules")
public class CustomSchedulesController {

    private final CustomSchedulesService customSchedulesService;
    private final TimeslotsService timeslotsService;

    @Autowired
    public CustomSchedulesController(CustomSchedulesService customSchedulesService, TimeslotsService timeslotsService) {
        this.customSchedulesService = customSchedulesService;
        this.timeslotsService = timeslotsService;
    }


    @GetMapping
    public String getCustomSchedules(Model model){
        model.addAttribute("customSchedules", customSchedulesService.getAllCustomSchedules());
        model.addAttribute("timeslotsForCustomSchedules", timeslotsService.getAll());

        return "customerService/customSchedulesList";
    }

    @GetMapping("/new")
    public String newCustomSchedule(Model model){
        model.addAttribute("newCustomSchedule", new CustomSchedules());
        model.addAttribute("timeslotsForCustomSchedules", timeslotsService.getAll());
        return "customerService/customSchedulesForm";
    }

    @PostMapping("/save")
    public String createCustomSchedule(@ModelAttribute CustomSchedules customSchedule){
        customSchedulesService.saveCustomSchedule(customSchedule);

        return "redirect:/custom-schedules";
    }

    @GetMapping("/{id}/edit")
    public String editCustomSchedule(@PathVariable("id") Long id, Model model){

        model.addAttribute("customScheduleToUpdate", customSchedulesService.getCustomSchedule(id));
        model.addAttribute("timeslotsForCustomSchedules", timeslotsService.getAll());
        return "customerService/customSchedulesEdit";
    }

    @PostMapping("{id}")
    public String updateCustomSchedule(@PathVariable("id") Long id,
                                       @ModelAttribute("customScheduleToUpdate") CustomSchedules customSchedule){
        CustomSchedules customScheduleToUpdate = customSchedulesService.getCustomSchedule(id);
        if (customScheduleToUpdate != null){
            customScheduleToUpdate.setCustom_date(customSchedule.getCustom_date());
            customScheduleToUpdate.setTimeslot(customSchedule.getTimeslot());

            customSchedulesService.saveCustomSchedule(customScheduleToUpdate);
        }

        return "redirect:/custom-schedules";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        customSchedulesService.deleteCustomSchedule(id);

        return "redirect:/custom-schedules";
    }
}
