package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TimeslotService timeslotService;
    private final ClientService clientService;
    private final CSService csService;

    private final AppointmentTimeslotService appointmentTimeslotService;
    @Autowired
    public AppointmentController(AppointmentService appointmentService, TimeslotService timeslotService,
                                 ClientService clientService, CSService csService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.timeslotService = timeslotService;
        this.clientService = clientService;
        this.csService = csService;

        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @GetMapping
    public String getAppointments(Model model) {
        model.addAttribute("appointments", appointmentTimeslotService.getAll());

        return "appointments/list";
    }


    @GetMapping("/new")
    public String newAppointment(Model model) {
        model.addAttribute("newAppointment", new Appointment());
        // TODO timeslotsService.getAll() заменить на вычисление свободных таймслотов
        model.addAttribute("timeslotsForAppointment", timeslotService.getAll());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("services", csService.getServices());

        return "appointments/form";
    }

    @PostMapping("/save")
    public String createAppointment(@ModelAttribute Appointment appointment,
                                    @ModelAttribute Timeslot timeslot) {
        appointmentService.save(appointment);

        return "redirect:/appointments";
    }

    @GetMapping("/{id}/edit")
    public String editAppointment(@PathVariable("id") Long id, Model model) {
        model.addAttribute("appointmentToUpdate", appointmentService.getAppointment(id));
        model.addAttribute("timeslotsForAppointment", timeslotService.getAll());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("servicesToUpdate", csService.getServices());

        return "appointments/edit";
    }

    @PostMapping("{id}")
    public String updateAppointment(@PathVariable("id") Long id,
                                    @ModelAttribute("appointmentToUpdate") Appointment appointment) {
        Appointment appointmentToUpdate = appointmentService.getAppointment(id);
        if (appointmentToUpdate != null) {
            appointmentToUpdate.setClient(appointment.getClient());
            appointmentToUpdate.setService(appointment.getService());
            appointmentToUpdate.setDateOfBooking(appointment.getDateOfBooking());

            appointmentService.save(appointmentToUpdate);
        }

        return "redirect:/appointments";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        appointmentService.delete(id);

        return "redirect:/appointments";
    }
}
