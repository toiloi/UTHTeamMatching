package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
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

}