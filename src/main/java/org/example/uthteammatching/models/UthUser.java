package org.example.uthteammatching.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "uthUser")
@Inheritance(strategy = InheritanceType.JOINED)
public class UthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maSo", nullable = false)
    private Long maSo;

    @Nationalized
    @Column(name = "ten", length = 50)
    private String ten;

    @Nationalized
    @Column(name = "ho", length = 50)
    private String ho;

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
    @Column(name = "username", length = 20, unique = true)
    private String username;

    @Nationalized
    @Column(name = "pass", length = 100)
    private String pass;

    @Nationalized
    @Column(name = "enabled")
    private Boolean enabled;

    // Thêm trường avatar
    @Nationalized
    @Column(name = "avatar", length = 255)
    private String avatar;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "userMaSo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ThanhvienProject> thanhVienProjects = new HashSet<>();

    // Constructor mặc định
    public UthUser() {
    }

    // Constructor đầy đủ
    public UthUser(Long maSo, String ten, String ho, String gioiTinh, String chuyenNganh, String email, String sdt,
                   String username, String pass, Boolean enabled, String avatar, Set<UserRole> userRoles) {
        this.maSo = maSo;
        this.ten = ten;
        this.ho = ho;
        this.gioiTinh = gioiTinh;
        this.chuyenNganh = chuyenNganh;
        this.email = email;
        this.sdt = sdt;
        this.username = username;
        this.pass = pass;
        this.enabled = enabled;
        this.avatar = avatar;
        this.userRoles = userRoles;
    }

    public UthUser(String username, String pass, String ho, String ten, String gioiTinh, String chuyenNganh, String email, String sdt) {
        this.username = username;
        this.pass = pass;
        this.ten = ten;
        this.ho = ho;
        this.gioiTinh = gioiTinh;
        this.chuyenNganh = chuyenNganh;
        this.email = email;
        this.sdt = sdt;
    }

    // Getters và Setters
    public Long getMaSo() {
        return maSo;
    }

    public void setMaSo(Long maSo) {
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}