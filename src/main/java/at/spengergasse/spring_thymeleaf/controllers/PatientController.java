package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/list")
    public String patients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "patlist";
    }

    @GetMapping("/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "add_patient";
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute("patient") Patient patient,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", result.getAllErrors().get(0).getDefaultMessage());
            return "add_patient";
        }

        try {
            // SVNR validieren (ruft setSvnr auf)
            patient.setSvnr(patient.getSvnr());
            // Geburtsdatum validieren (ruft setBirth auf)
            patient.setBirth(patient.getBirth());

            patientRepository.save(patient);
            return "redirect:/patient/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "add_patient";
        }
    }

    // Datenbankfehler abfangen (z.B. MySQL läuft nicht)
    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException ex, Model model) {
        model.addAttribute("errorMessage",
                "Datenbankfehler: Die Verbindung zur Datenbank ist fehlgeschlagen. " +
                        "Bitte stellen Sie sicher, dass MySQL läuft und versuchen Sie es erneut. " +
                        "(Details: " + ex.getMostSpecificCause().getMessage() + ")");
        model.addAttribute("backLink", "/patient/add");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("backLink", "/patient/add");
        return "error";
    }
}