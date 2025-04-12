package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class BaiViet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private org.example.uthteammatching.models.UthUser userMaSo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "projectMaSo")
    private org.example.uthteammatching.models.Project projectMaSo;

    public BaiViet(String noiDung, Instant ngayDang, org.example.uthteammatching.models.UthUser userMaSo, org.example.uthteammatching.models.Project projectMaSo) {
        this.noiDung = noiDung;
        this.ngayDang = ngayDang;
        this.userMaSo = userMaSo;
        this.projectMaSo = projectMaSo;
    }

    public BaiViet() {

    }
}