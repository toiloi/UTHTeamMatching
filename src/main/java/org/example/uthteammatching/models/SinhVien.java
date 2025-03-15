package org.example.uthteammatching.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private org.example.uthteammatching.models.U us;

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

    public String getMaSo() {
        return maSo;
    }

    public void setMaSo(String maSo) {
        this.maSo = maSo;
    }

    public org.example.uthteammatching.models.U getUs() {
        return us;
    }

    public void setUs(org.example.uthteammatching.models.U us) {
        this.us = us;
    }

    public Integer getMucDoUyTin() {
        return mucDoUyTin;
    }

    public void setMucDoUyTin(Integer mucDoUyTin) {
        this.mucDoUyTin = mucDoUyTin;
    }

    public String getKyNang() {
        return kyNang;
    }

    public void setKyNang(String kyNang) {
        this.kyNang = kyNang;
    }

    public Short getThoiGianRanhTuan() {
        return thoiGianRanhTuan;
    }

    public void setThoiGianRanhTuan(Short thoiGianRanhTuan) {
        this.thoiGianRanhTuan = thoiGianRanhTuan;
    }

    public Short getThoiGianRanhNgay() {
        return thoiGianRanhNgay;
    }

    public void setThoiGianRanhNgay(Short thoiGianRanhNgay) {
        this.thoiGianRanhNgay = thoiGianRanhNgay;
    }

}