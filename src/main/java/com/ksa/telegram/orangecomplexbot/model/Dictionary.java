package com.ksa.telegram.orangecomplexbot.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String matcher;
    @Column(name = "data", length = 4000)
    private String data;

}
