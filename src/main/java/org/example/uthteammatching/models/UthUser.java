package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "uthUser")
public class UthUser {
    @Id
    @Nationalized
    @Column(name = "maSo", nullable = false, length = 50)
    private String maSo;

    @Nationalized
    @Column(name = "ten", length = 50)
    private String ten;

    @Nationalized
    @Column(name = "ho", length = 50)
    private String ho;

    @Column(name = "roleId", columnDefinition = "tinyint")
    private Short roleId;

    @Nationalized
    @Column(name = "gioiTinh", length = 10)
    private String gioiTinh;

    @Nationalized
    @Column(name = "chuyenNganh", length = 100)
    private String chuyenNganh;

    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Nationalized
    @Column(name = "sdt", length = 15)
    private String sdt;

    @Nationalized
    @Column(name = "username", length = 20)
    private String username;

    @Nationalized
    @Column(name = "pass", length = 20)
    private String pass;

    public String getMaSo() {
        return maSo;
    }

    public void setMaSo(String maSo) {
        this.maSo = maSo;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public Short getRoleId() {
        return roleId;
    }

    public void setRoleId(Short roleId) {
        this.roleId = roleId;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChuyenNganh() {
        return chuyenNganh;
    }

    public void setChuyenNganh(String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}