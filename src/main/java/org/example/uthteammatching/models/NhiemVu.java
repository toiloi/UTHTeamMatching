package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
public class NhiemVu {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date ngayBatDau;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date ngayKetThuc;

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

}