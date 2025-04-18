package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Project {

    public Long getMaProject() {
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

    public void setMaProject(Long maProject) {
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maProject;

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

    @OneToMany(mappedBy = "projectMaSo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThanhvienProject> thanhVienProjects = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GiangVien")
    private UthUser GiangVien;

    @Nationalized
    @Column(name = "loai", length = 20)
    private String loai;

    @Nationalized
    @Column(name = "nhanXet", length = 500)
    private String  nhanXet;

    @Nationalized
    @Column(name = "diem")
    private Integer  diem;

    public Project(){}
    public Project(String tenProject, String moTa, LocalDate ngayBatDau, LocalDate ngayKetThuc, String trangThai, UthUser GiangVien, String loai, int diem, String nhanXet, LocalDateTime ngayTao) {
        this.tenProject = tenProject;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.GiangVien = GiangVien;
        this.loai = loai;
        this.diem = diem;
        this.nhanXet = nhanXet;
        this.ngayTao = ngayTao;
    }

    public void addThanhVien(ThanhvienProject tv) {
        thanhVienProjects.add(tv);
        tv.setProjectMaSo(this);
    }

    public void removeThanhVien(ThanhvienProject tv) {
        thanhVienProjects.remove(tv);
        tv.setProjectMaSo(null);
    }


}