package com.tfm.ms_company_service.service;

import com.tfm.ms_company_service.model.Company;
import com.tfm.ms_company_service.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @CacheEvict(cacheNames = "companies", key = "'allCompanies'")
    public ResponseEntity createCompany(Company company) {
        if(!companyRepository.findByNameOrCif(company.getName(), company.getCif()).isEmpty()) {
            return new ResponseEntity<>("Company name/cif already exist", HttpStatus.BAD_REQUEST);
        }
        companyRepository.save(company);
        log.info("Company saved");
        return new ResponseEntity<>("Company created", HttpStatus.CREATED);
    }

    @Cacheable(cacheNames = "companies", key = "'allCompanies'")
    public List<Company> getAll(){
        log.info("Companies not in cache, searching all in DB");
        return companyRepository.findAll();
    }

    @Cacheable(cacheNames = "company", key="#id", condition = "#id!=null")
    public Company getCompany(String id) {
        log.info("Company not in cache, searching in DB");
        Optional<Company> optCompany = companyRepository.findById(id);
        if(optCompany.isPresent()) {
            return optCompany.get();
        }else{
            return null;
        }
    }
    @CachePut(cacheNames = "company", key = "#company.id")
    @CacheEvict(cacheNames = "companies", key = "'allCompanies'")
    public ResponseEntity updateCompany(Company company) {
        Company companyUpdated = companyRepository.save(company);
        log.info("Company updated");
        return new ResponseEntity<>(companyUpdated, HttpStatus.OK);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "company", key = "#id"),
            @CacheEvict(cacheNames = "companies", key = "'allCompanies'")
    })
    public ResponseEntity deleteCompany(String id) {
        Optional<Company> optCompany = companyRepository.findById(id);
        if(!optCompany.isPresent()) {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
        companyRepository.delete(optCompany.get());
        return new ResponseEntity<>("Company deleted",HttpStatus.OK);
    }
}
