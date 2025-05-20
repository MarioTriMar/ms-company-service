package com.tfm.ms_company_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value="companyEntity")
public class Company {

    @Id
    private String id;
    private String name;
    private String address;
    private String supportPhone;
    private String email;
    private String cif;
}

