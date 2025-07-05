package com.tfm.ms_company_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(value="companyEntity")
public class Company implements Serializable {

    @Id
    private String id;
    private String name;
    private String address;
    private String supportPhone;
    private String email;
    private String cif;
}

