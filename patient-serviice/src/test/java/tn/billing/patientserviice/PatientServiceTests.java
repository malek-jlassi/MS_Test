package tn.billing.patientserviice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tn.billing.patientserviice.entities.Patient;
import tn.billing.patientserviice.repositories.PatientRepository;
import tn.billing.patientserviice.services.PatientService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PatientServiceTests {

    @Autowired
    private PatientService patientService;

    @MockBean
    private PatientRepository patientRepository;

    @Test
    void contextLoads() {
        assertNotNull(patientService);
    }

    @Test
    void shouldCreatePatient() {
        // Arrange
        Patient patient = new Patient();
        patient.setName("Test Patient");
        patient.setEmail("test@example.com");

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        Patient savedPatient = patientService.createPatient(patient);

        // Assert
        assertNotNull(savedPatient);
        assertEquals("Test Patient", savedPatient.getName());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldGetAllPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(
            new Patient("Patient 1", "patient1@example.com"),
            new Patient("Patient 2", "patient2@example.com")
        );
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(patientRepository).findAll();
    }

    @Test
    void shouldGetPatientById() {
        // Arrange
        Patient patient = new Patient("Test Patient", "test@example.com");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Act
        Optional<Patient> result = patientService.getPatientById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Patient", result.get().getName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void shouldUpdatePatient() {
        // Arrange
        Patient patient = new Patient("Old Name", "old@example.com");
        Patient updatedPatient = new Patient("New Name", "new@example.com");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        // Act
        Optional<Patient> result = patientService.updatePatient(1L, updatedPatient);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldDeletePatient() {
        // Arrange
        Patient patient = new Patient("Test Patient", "test@example.com");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).deleteById(1L);

        // Act
        boolean result = patientService.deletePatient(1L);

        // Assert
        assertTrue(result);
        verify(patientRepository).deleteById(1L);
    }
} 