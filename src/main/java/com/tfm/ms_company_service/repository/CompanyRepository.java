package com.tfm.ms_company_service.repository;

import com.tfm.ms_company_service.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    List<Company> findByNameOrCif(String name, String cif);

}
