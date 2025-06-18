package com.tfm.ms_company_service;

import com.tfm.ms_company_service.model.Company;
import com.tfm.ms_company_service.repository.CompanyRepository;
import com.tfm.ms_company_service.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void shouldReturnBadRequestWhenCompanyAlreadyExists() {
        Company company = new Company();
        company.setName("Acme");
        company.setCif("A12345678");

        when(companyRepository.findByNameOrCif("Acme", "A12345678"))
                .thenReturn(List.of(new Company()));

        ResponseEntity<?> response = companyService.createCompany(company);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Company name/cif already exist", response.getBody());
    }

    @Test
    void shouldCreateCompanySuccessfully() {
        Company company = new Company();
        company.setName("Acme");
        company.setCif("A12345678");

        when(companyRepository.findByNameOrCif("Acme", "A12345678"))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = companyService.createCompany(company);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Company created", response.getBody());
        verify(companyRepository).save(company);
    }

    @Test
    void shouldReturnCompanyWhenExists() {
        Company company = new Company();
        company.setId("123");

        when(companyRepository.findById("123"))
                .thenReturn(Optional.of(company));

        ResponseEntity<?> response = companyService.getCompany("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenCompanyDoesNotExist() {
        when(companyRepository.findById("not_found"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = companyService.getCompany("not_found");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Company not found", response.getBody());
    }
    @Test
    void shouldUpdateCompanySuccessfully() {
        Company company = new Company();
        company.setId("123");
        company.setName("Updated Name");

        when(companyRepository.save(company)).thenReturn(company);

        ResponseEntity<?> response = companyService.updateCompany(company);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company, response.getBody());
        verify(companyRepository).save(company);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentCompany() {
        when(companyRepository.findById("not_found"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = companyService.deleteCompany("not_found");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Company not found", response.getBody());
    }

    @Test
    void shouldDeleteCompanySuccessfully() {
        Company company = new Company();
        company.setId("123");

        when(companyRepository.findById("123"))
                .thenReturn(Optional.of(company));

        ResponseEntity<?> response = companyService.deleteCompany("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Company deleted", response.getBody());
        verify(companyRepository).delete(company);
    }


}
