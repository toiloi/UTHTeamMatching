package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

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

    public void setMaProject(Long maProject) {
        this.maProject = maProject;
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

    @JoinColumn(name = "maGiangVien")
    private Long maGiangVien;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai", nullable = false)
    private ProjectType loai;

    @Nationalized
    @Column(name = "nhanXet", length = 500)
    private String nhanXet;

    @Column(name = "diem")
    private Integer diem;

    @OneToMany(mappedBy = "projectMaSo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThanhvienProject> thanhVienProjects = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaiLieu> taiLieus = new ArrayList<>();

    public Project() {}

    public Project(String tenProject, String moTa, LocalDate ngayBatDau, LocalDate ngayKetThuc, String trangThai, Long maGiangVien, ProjectType loai, Integer diem, String nhanXet, LocalDateTime ngayTao) {
        this.tenProject = tenProject;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.maGiangVien = maGiangVien;
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

    public void addTaiLieu(TaiLieu taiLieu) {
        taiLieus.add(taiLieu);
        taiLieu.setProject(this);
    }

    public void removeTaiLieu(TaiLieu taiLieu) {
        taiLieus.remove(taiLieu);
        taiLieu.setProject(null);
    }
}