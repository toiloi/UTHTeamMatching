package org.example.uthteammatching.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
public class BaiViet {
    @Id
    @Nationalized
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Nationalized
    @Lob
    @Column(name = "noiDung", nullable = false)
    private String noiDung;

    @ColumnDefault("getdate()")
    @Column(name = "ngayDang")
    private Instant ngayDang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userMaSo", nullable = false)
    private org.example.uthteammatching.models.U userMaSo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "projectMaSo")
    private org.example.uthteammatching.models.Project projectMaSo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Instant getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(Instant ngayDang) {
        this.ngayDang = ngayDang;
    }

    public org.example.uthteammatching.models.U getUserMaSo() {
        return userMaSo;
    }

    public void setUserMaSo(org.example.uthteammatching.models.U userMaSo) {
        this.userMaSo = userMaSo;
    }

    public org.example.uthteammatching.models.Project getProjectMaSo() {
        return projectMaSo;
    }

    public void setProjectMaSo(org.example.uthteammatching.models.Project projectMaSo) {
        this.projectMaSo = projectMaSo;
    }

}