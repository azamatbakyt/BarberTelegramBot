package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.entity.Appointment;
import kz.azamatbakyt.BarberTelegramBot.entity.Timeslot;
import kz.azamatbakyt.BarberTelegramBot.helpers.Status;
import kz.azamatbakyt.BarberTelegramBot.repository.AppointmentTimeslotRepository;
import kz.azamatbakyt.BarberTelegramBot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TimeslotService timeslotService;
    private final ClientService clientService;
    private final CSService csService;
    private final ScheduleService scheduleService;
    private final AppointmentTimeslotService appointmentTimeslotService;
    @Autowired
    public AppointmentController(AppointmentService appointmentService, TimeslotService timeslotService,
                                 ClientService clientService, CSService csService, ScheduleService scheduleService, AppointmentTimeslotService appointmentTimeslotService) {
        this.appointmentService = appointmentService;
        this.timeslotService = timeslotService;
        this.clientService = clientService;
        this.csService = csService;
        this.scheduleService = scheduleService;

        this.appointmentTimeslotService = appointmentTimeslotService;
    }

    @GetMapping
    public String getAppointments(Model model) {
        Locale locale_ru = new Locale("ru", "RU");
        model.addAttribute("appointments", appointmentTimeslotService.getAllSuccessfulAppointments(Status.BOOKING_SUCCESSFUL));
        model.addAttribute("getDays", scheduleService.getDays());
        model.addAttribute("showAllAppointments", true);
        return "appointments/list";
    }

    @GetMapping("/bydate")
    public String getAppointmentsByDate(Model model, @RequestParam("date")LocalDate date) throws ParseException {
        model.addAttribute("appointmentByDate", appointmentTimeslotService.getAllByDate(date));
        model.addAttribute("getDays", scheduleService.getDays());
        model.addAttribute("showAllAppointments", false);
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

    @ExceptionHandler(TemplateProcessingException.class)
    public String errorPage(){
        return "error";
    }
}
