package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import at.spengergasse.spring_thymeleaf.entities.ReservationRepository;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import at.spengergasse.spring_thymeleaf.entities.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationController
{
    private final ReservationRepository reservationrepository;

    public ReservationController(ReservationRepository reservationrepository) {
        this.reservationrepository = reservationrepository;
    }

    @GetMapping("/list")
    public String reservation(Model model)
    {
        model.addAttribute("reservation",reservationrepository.findAll());
        return "reservationlist";
    }
    @GetMapping("/add")
    public String addReservation(Model model)
    {
        model.addAttribute("reservation",new Reservation());
        return "add_reservation";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute("reservation") Reservation reservation)
    {
        reservationrepository.save(reservation);
        return "redirect:/reservation/list";
    }
}
