package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

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

    public String getMaProject() {
        return maProject;
    }

    public void setMaProject(String maProject) {
        this.maProject = maProject;
    }

    public String getTenProject() {
        return tenProject;
    }

    public void setTenProject(String tenProject) {
        this.tenProject = tenProject;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}