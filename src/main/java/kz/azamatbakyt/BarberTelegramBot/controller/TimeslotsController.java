package kz.azamatbakyt.BarberTelegramBot.controller;


import kz.azamatbakyt.BarberTelegramBot.entity.Timeslots;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/timeslots")
public class TimeslotsController {

    private final TimeslotsService timeslotsService;

    @Autowired
    public TimeslotsController(TimeslotsService timeslotsService) {
        this.timeslotsService = timeslotsService;
    }

    @GetMapping
    public String getTimeSlots(Model model) {
        model.addAttribute("timeslots", timeslotsService.getAll());

        return "customerService/timeslotsList";
    }

    @GetMapping("/new")
    public String getCreateTimeslotForm(Model model) {
        model.addAttribute("newTimeSlot", new Timeslots());
        return "customerService/timeslotForm";
    }

    @PostMapping("/save")
    public String saveTimeslot(@ModelAttribute Timeslots timeslot) {
        timeslotsService.saveTimeslot(timeslot);

        return "redirect:/timeslots";
    }

    @GetMapping("/{id}/edit")
    public String editTimeslot(@PathVariable("id") Long id, Model model) {
        Timeslots timeslot = timeslotsService.getTimeslot(id);
        model.addAttribute("timeslotToUpdate", timeslot);

        return "customerService/timeslotEdit";
    }

    @PostMapping("/{id}")
    public String updateTimeslot(@PathVariable("id") Long id,
                                 @ModelAttribute("timeslotToUpdate") Timeslots updatedTimeslot) {
        Timeslots timeslot = timeslotsService.getTimeslot(id);
        if (timeslot != null) {
            timeslot.setStart_time(updatedTimeslot.getStart_time());
            timeslot.setEnd_time(updatedTimeslot.getEnd_time());

            timeslotsService.saveTimeslot(timeslot);
        }


        return "redirect:/timeslots";
    }

    @GetMapping("/{id}/delete")
    public String deleteTimeslot(@PathVariable("id") Long id) {
        timeslotsService.deleteTimeslot(id);
        return "redirect:/timeslots";
    }

}
