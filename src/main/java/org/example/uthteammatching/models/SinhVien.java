package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class SinhVien extends UthUser{

    @ColumnDefault("100")
    @Column(name = "mucDoUyTin")
    private Integer mucDoUyTin;

    @Nationalized
    @Lob
    @Column(name = "kyNang")
    private String kyNang;

    @ColumnDefault("0")
    @Column(name = "thoiGianRanhTuan", columnDefinition = "tinyint")
    private Short thoiGianRanhTuan;

    @ColumnDefault("0")
    @Column(name = "thoiGianRanhNgay", columnDefinition = "tinyint")
    private Short thoiGianRanhNgay;

}