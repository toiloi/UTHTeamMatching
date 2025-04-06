package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Project {
    @Id
    @Nationalized
    @Column(name = "maProject", nullable = false, length = 50)
    private String maProject;

    @Nationalized
    @Column(name = "tenProject", nullable = false, length = 100)
    private String tenProject;

    @Nationalized
    @Lob
    @Column(name = "moTa")
    private String moTa;

    @Column(name = "ngayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDate ngayKetThuc;

    @Nationalized
    @Column(name = "trangThai", length = 20)
    private String trangThai;

}