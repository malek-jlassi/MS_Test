package tn.billing.facturationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tn.billing.facturationservice.entities.Facture;
import tn.billing.facturationservice.repositories.FactureRepository;
import tn.billing.facturationservice.services.FactureService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FacturationServiceTests {

    @Autowired
    private FactureService factureService;

    @MockBean
    private FactureRepository factureRepository;

    @Test
    void contextLoads() {
        assertNotNull(factureService);
    }

    @Test
    void shouldCreateFacture() {
        // Arrange
        Facture facture = new Facture();
        facture.setAmount(new BigDecimal("100.00"));
        facture.setPatientId(1L);
        facture.setDate(LocalDate.now());

        when(factureRepository.save(any(Facture.class))).thenReturn(facture);

        // Act
        Facture savedFacture = factureService.createFacture(facture);

        // Assert
        assertNotNull(savedFacture);
        assertEquals(new BigDecimal("100.00"), savedFacture.getAmount());
        assertEquals(1L, savedFacture.getPatientId());
        verify(factureRepository).save(any(Facture.class));
    }

    @Test
    void shouldGetAllFactures() {
        // Arrange
        List<Facture> factures = Arrays.asList(
            new Facture(1L, new BigDecimal("100.00"), LocalDate.now()),
            new Facture(2L, new BigDecimal("200.00"), LocalDate.now())
        );
        when(factureRepository.findAll()).thenReturn(factures);

        // Act
        List<Facture> result = factureService.getAllFactures();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(factureRepository).findAll();
    }

    @Test
    void shouldGetFactureById() {
        // Arrange
        Facture facture = new Facture(1L, new BigDecimal("100.00"), LocalDate.now());
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        // Act
        Optional<Facture> result = factureService.getFactureById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("100.00"), result.get().getAmount());
        verify(factureRepository).findById(1L);
    }

    @Test
    void shouldGetFacturesByPatientId() {
        // Arrange
        List<Facture> factures = Arrays.asList(
            new Facture(1L, new BigDecimal("100.00"), LocalDate.now()),
            new Facture(1L, new BigDecimal("200.00"), LocalDate.now())
        );
        when(factureRepository.findByPatientId(1L)).thenReturn(factures);

        // Act
        List<Facture> result = factureService.getFacturesByPatientId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(factureRepository).findByPatientId(1L);
    }

    @Test
    void shouldUpdateFacture() {
        // Arrange
        Facture facture = new Facture(1L, new BigDecimal("100.00"), LocalDate.now());
        Facture updatedFacture = new Facture(1L, new BigDecimal("150.00"), LocalDate.now());
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));
        when(factureRepository.save(any(Facture.class))).thenReturn(updatedFacture);

        // Act
        Optional<Facture> result = factureService.updateFacture(1L, updatedFacture);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("150.00"), result.get().getAmount());
        verify(factureRepository).save(any(Facture.class));
    }

    @Test
    void shouldDeleteFacture() {
        // Arrange
        Facture facture = new Facture(1L, new BigDecimal("100.00"), LocalDate.now());
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));
        doNothing().when(factureRepository).deleteById(1L);

        // Act
        boolean result = factureService.deleteFacture(1L);

        // Assert
        assertTrue(result);
        verify(factureRepository).deleteById(1L);
    }
} 