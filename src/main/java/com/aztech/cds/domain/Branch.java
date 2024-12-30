package com.aztech.cds.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BRANCH_MASTER")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BRANCH_MASTER_generator")
    @SequenceGenerator(name = "BRANCH_MASTER_generator", allocationSize = 1, initialValue = 1, sequenceName = "BRANCH_MASTER_SEQ")
    private long id;

    @Column(name = "BRANCH_CODE")
    private String branchCode;
    @Column(name = "BRANCH_NAME")
    private String branchName;
    @Column(name = "BRANCH_NAME_AR")
    private String branchNameAr;
    @Column(name = "BRANCH_STS")
    private Boolean branchStatus;
    @Column(name = "BRANCH_CODE_NO")
    private String branchCodeNo;

}

