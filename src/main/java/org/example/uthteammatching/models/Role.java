package org.example.uthteammatching.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "maSo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maSo;
    @Column(name = "ten")
    private String ten;

    @JsonManagedReference
    @OneToMany(mappedBy = "role")
    private Set<UserRole> roleUsers;

    public Role() {

    }

    public Role(Long maSo, String ten, Set<UserRole> roleUsers) {
        this.maSo = maSo;
        this.ten = ten;
        this.roleUsers = roleUsers;
    }

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

    public Set<UserRole> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(Set<UserRole> roleUsers) {
        this.roleUsers = roleUsers;
    }
}
