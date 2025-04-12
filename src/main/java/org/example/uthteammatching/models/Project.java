package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Project {
    public String getMaProject() {
        return maProject;
    }

    public String getTenProject() {
        return tenProject;
    }

    public String getMoTa() {
        return moTa;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setMaProject(String maProject) {
        this.maProject = maProject;
    }

    public void setTenProject(String tenProject) {
        this.tenProject = tenProject;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

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
    @Column(name = "ngayTao", updatable = false)
    private LocalDateTime ngayTao = LocalDateTime.now();
}