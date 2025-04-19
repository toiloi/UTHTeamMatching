package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
public class TaiLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(name = "tenTaiLieu", nullable = false, length = 100)
    private String tenTaiLieu; // Tên tài liệu (ví dụ: "Yêu cầu dự án.pdf")

    @Nationalized
    @Column(name = "duongDan", length = 255)
    private String duongDan; // Đường dẫn hoặc URL đến tài liệu

    @Nationalized
    @Column(name = "loaiTaiLieu", length = 50)
    private String loaiTaiLieu; // Loại tài liệu (ví dụ: "PDF", "Word")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectMaSo", nullable = false)
    private Project project; // Liên kết với Project

    public TaiLieu() {}

    public TaiLieu(String tenTaiLieu, String duongDan, String loaiTaiLieu, Project project) {
        this.tenTaiLieu = tenTaiLieu;
        this.duongDan = duongDan;
        this.loaiTaiLieu = loaiTaiLieu;
        this.project = project;
    }
}