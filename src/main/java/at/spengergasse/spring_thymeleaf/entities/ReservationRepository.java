package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByDeviceId(Integer deviceId);
    List<Reservation> findByDeviceIdAndDate(Integer deviceId, LocalDate date);
    List<Reservation> findByPatientIdAndDate(Integer patientId, LocalDate date);
}