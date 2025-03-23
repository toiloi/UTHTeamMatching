package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class SinhVien {
    @Id
    @Nationalized
    @Column(name = "maSo", nullable = false, length = 50)
    private String maSo;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "maSo", nullable = false)
    private org.example.uthteammatching.models.UthUser uthUser;

    @Column(name = "mucDoUyTin")
    private Integer mucDoUyTin;

    @Nationalized
    @Lob
    @Column(name = "kyNang")
    private String kyNang;

    @Column(name = "thoiGianRanhTuan", columnDefinition = "tinyint")
    private Short thoiGianRanhTuan;

    @Column(name = "thoiGianRanhNgay", columnDefinition = "tinyint")
    private Short thoiGianRanhNgay;

}