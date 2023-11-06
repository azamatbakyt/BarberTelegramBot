package kz.azamatbakyt.BarberTelegramBot.controller;


import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/timeslots")
public class TimeslotController {

    private final TimeslotService timeslotService;

    @Autowired
    public TimeslotController(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    @GetMapping
    public String getTimeSlots(Model model) {
        model.addAttribute("timeslots", timeslotService.getAll());

        return "timeslots/list";
    }

    @GetMapping("/new")
    public String getCreateTimeslotForm(Model model) {
        model.addAttribute("newTimeSlot", new Timeslot());
        return "timeslots/form";
    }

    @PostMapping("/save")
    public String saveTimeslot(@ModelAttribute Timeslot timeslot) {
        timeslotService.saveTimeslot(timeslot);

        return "redirect:/timeslots";
    }

    @GetMapping("/{id}/edit")
    public String editTimeslot(@PathVariable("id") Long id, Model model) {
        Timeslot timeslot = timeslotService.getTimeslot(id);
        model.addAttribute("timeslotToUpdate", timeslot);

        return "timeslots/edit";
    }

    @PostMapping("/{id}")
    public String updateTimeslot(@PathVariable("id") Long id,
                                 @ModelAttribute("timeslotToUpdate") Timeslot updatedTimeslot) {
        Timeslot timeslot = timeslotService.getTimeslot(id);
        if (timeslot != null) {
            timeslot.setStartTime(updatedTimeslot.getStartTime());
            timeslot.setEndTime(updatedTimeslot.getEndTime());

            timeslotService.saveTimeslot(timeslot);
        }


        return "redirect:/timeslots";
    }

    @GetMapping("/{id}/delete")
    public String deleteTimeslot(@PathVariable("id") Long id) {
        timeslotService.deleteTimeslot(id);
        return "redirect:/timeslots";
    }

}
