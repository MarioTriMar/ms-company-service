package com.tfm.ms_company_service.controller;

import com.tfm.ms_company_service.model.Company;
import com.tfm.ms_company_service.service.CompanyService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/company")
@RestController
@Slf4j
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @PostMapping()
    public ResponseEntity createCompany(@RequestBody Company company) {
        log.info("CreateCompany [{}]", company.toString());
        if(!isValid(company)){
            return new ResponseEntity<>("Invalid company", HttpStatus.BAD_REQUEST);
        }
        return companyService.createCompany(company);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCompany(@PathVariable String id) {
        log.info("Searching company by ID: {}", id);
        if(id==null || id.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Company company = companyService.getCompany(id);
        if (company==null){
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity updateCompany(@RequestBody Company company) {
        log.info("Update company [{}]", company);
        if(company.getEmail()==null || company.getName()==null ||
            company.getAddress()==null || company.getSupportPhone()==null || company.getId()==null ||
            company.getCif() == null){
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
        }
        if(!isValidEmail(company)){
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }
        return companyService.updateCompany(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCompany(@PathVariable String id) {
        log.info("Delete company by ID: {}", id);
        if(id==null || id.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return companyService.deleteCompany(id);
    }

    private boolean isValid(Company company){
        if(company.getId() != null || company.getCif()==null || company.getName()==null){
            return false;
        }
        return isValidEmail(company);
    }

    private boolean isValidEmail(Company company){
        if (company.getEmail() == null) return false;
        Matcher matcher = EMAIL_PATTERN.matcher(company.getEmail());
        return matcher.matches();
    }
}
