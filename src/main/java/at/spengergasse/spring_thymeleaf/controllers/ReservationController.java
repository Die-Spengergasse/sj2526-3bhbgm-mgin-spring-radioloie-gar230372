package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import at.spengergasse.spring_thymeleaf.entities.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Controller
@RequestMapping("/reservation")
public class ReservationController
{
    private final ReservationRepository reservationrepository;
    private final PatientRepository patientrepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository reservationrepository, PatientRepository patientrepository, DeviceRepository deviceRepository) {
        this.reservationrepository = reservationrepository;
        this.patientrepository = patientrepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/list")
    public String reservations(Model model, Integer deviceId)
    {
        if(deviceId != null){
            model.addAttribute("reservations",reservationrepository.findByDeviceId(deviceId));
        }else{
            model.addAttribute("reservations",reservationrepository.findAll());
        }
        model.addAttribute("devices",deviceRepository.findAll());
        return "reslist";
    }
    @GetMapping("/add")
    public String addReservation(Model model)
    {
        model.addAttribute("reservation",new Reservation());
        model.addAttribute("patients",patientrepository.findAll());
        model.addAttribute("devices",deviceRepository.findAll());
        return "add_reservation";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute("reservation") Reservation reservation, Model model)
    {
        LocalTime newStart = reservation.getTime();
        LocalTime newEnd = newStart.plusMinutes(30);

        for (Reservation r : reservationrepository.findByDeviceIdAndDate(reservation.getDevice().getId(), reservation.getDate()))
        {
            LocalTime existingStart = r.getTime();
            LocalTime existingEnd = existingStart.plusMinutes(30);

            boolean overlap = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

            if (overlap) {
                model.addAttribute("errorMessage",
                        "Gerät bereits reserviert von " + existingStart + " bis " + existingEnd + "!");
                model.addAttribute("reservation", reservation);
                model.addAttribute("patients", patientrepository.findAll());
                model.addAttribute("devices", deviceRepository.findAll());
                return "add_reservation";
            }
        }
        reservationrepository.save(reservation);
        return "redirect:/reservation/list";
    }

}
