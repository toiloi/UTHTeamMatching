package org.example.uthteammatching.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
public class NhiemVu {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "deadline", nullable = false)
    private Instant deadline;

    @Nationalized
    @Lob
    @Column(name = "noiDung", nullable = false)
    private String noiDung;

    @ColumnDefault("0")
    @Column(name = "tinhTrang")
    private Boolean tinhTrang;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "projectMaSo")
    private org.example.uthteammatching.models.Project projectMaSo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private org.example.uthteammatching.models.ThanhvienProject thanhvienProject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Boolean getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(Boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public org.example.uthteammatching.models.Project getProjectMaSo() {
        return projectMaSo;
    }

    public void setProjectMaSo(org.example.uthteammatching.models.Project projectMaSo) {
        this.projectMaSo = projectMaSo;
    }

    public org.example.uthteammatching.models.ThanhvienProject getThanhvienProject() {
        return thanhvienProject;
    }

    public void setThanhvienProject(org.example.uthteammatching.models.ThanhvienProject thanhvienProject) {
        this.thanhvienProject = thanhvienProject;
    }

}