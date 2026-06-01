package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationRepository reservationrepository;
    private final PatientRepository patientrepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository reservationrepository,
                                 PatientRepository patientrepository,
                                 DeviceRepository deviceRepository) {
        this.reservationrepository = reservationrepository;
        this.patientrepository = patientrepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/list")
    public String reservations(Model model, Integer deviceId) {
        if (deviceId != null) {
            model.addAttribute("reservations", reservationrepository.findByDeviceId(deviceId));
        } else {
            model.addAttribute("reservations", reservationrepository.findAll());
        }
        model.addAttribute("devices", deviceRepository.findAll());
        return "reslist";
    }

    @GetMapping("/add")
    public String addReservation(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientrepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "add_reservation";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute("reservation") Reservation reservation, Model model) {

        LocalDate reservationDate = reservation.getDate();
        LocalTime newStart = reservation.getTime();
        LocalTime newEnd = newStart.plusMinutes(30);

        // --- Prüfung 1: Termin in der Vergangenheit ---
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, newStart);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            model.addAttribute("errorMessage",
                    "Ungültiger Termin: Der Termin (" + reservationDate + " um " + newStart +
                            " Uhr) liegt in der Vergangenheit. Bitte wählen Sie einen zukünftigen Termin.");
            model.addAttribute("reservation", reservation);
            model.addAttribute("patients", patientrepository.findAll());
            model.addAttribute("devices", deviceRepository.findAll());
            return "add_reservation";
        }

        // --- Prüfung 2: Gerät bereits zur selben Zeit reserviert ---
        for (Reservation r : reservationrepository.findByDeviceIdAndDate(
                reservation.getDevice().getId(), reservationDate)) {

            LocalTime existingStart = r.getTime();
            LocalTime existingEnd = existingStart.plusMinutes(30);
            boolean overlap = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

            if (overlap) {
                model.addAttribute("errorMessage",
                        "Terminüberschneidung (Gerät): Das gewählte Gerät ist am " + reservationDate +
                                " bereits von " + existingStart + " bis " + existingEnd +
                                " Uhr reserviert. Bitte wählen Sie eine andere Uhrzeit.");
                model.addAttribute("reservation", reservation);
                model.addAttribute("patients", patientrepository.findAll());
                model.addAttribute("devices", deviceRepository.findAll());
                return "add_reservation";
            }
        }

        // --- Prüfung 3: Patient hat zur selben Zeit bereits einen anderen Termin ---
        for (Reservation r : reservationrepository.findByPatientIdAndDate(
                reservation.getPatient().getId(), reservationDate)) {

            LocalTime existingStart = r.getTime();
            LocalTime existingEnd = existingStart.plusMinutes(30);
            boolean overlap = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

            if (overlap) {
                model.addAttribute("errorMessage",
                        "Terminüberschneidung (Patient): Der gewählte Patient hat am " + reservationDate +
                                " bereits einen Termin von " + existingStart + " bis " + existingEnd +
                                " Uhr. Bitte wählen Sie eine andere Uhrzeit.");
                model.addAttribute("reservation", reservation);
                model.addAttribute("patients", patientrepository.findAll());
                model.addAttribute("devices", deviceRepository.findAll());
                return "add_reservation";
            }
        }

        reservationrepository.save(reservation);
        return "redirect:/reservation/list";
    }

    // Datenbankfehler abfangen (z.B. MySQL läuft nicht)
    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException ex, Model model) {
        model.addAttribute("errorMessage",
                "Datenbankfehler: Die Verbindung zur Datenbank ist fehlgeschlagen. " +
                        "Bitte stellen Sie sicher, dass MySQL läuft und versuchen Sie es erneut. " +
                        "(Details: " + ex.getMostSpecificCause().getMessage() + ")");
        model.addAttribute("backLink", "/reservation/add");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("backLink", "/reservation/add");
        return "error";
    }
}