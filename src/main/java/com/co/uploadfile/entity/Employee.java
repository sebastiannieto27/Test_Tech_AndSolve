package com.co.uploadfile.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends AbstractAuditingEntity {

    @Id
    @NonNull
    private Long id;

    @NonNull
    private String name;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @NonNull
    private byte[] data;
}
