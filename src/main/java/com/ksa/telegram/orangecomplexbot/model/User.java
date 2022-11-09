package com.ksa.telegram.orangecomplexbot.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name="users")
@Getter
@Setter

public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp dateReg;
    private String rolies;

    public boolean checkRole(String role){
        String rolies_ = "," + rolies + "," ;
        return rolies_.contains("," + role + ",");
    }
}
