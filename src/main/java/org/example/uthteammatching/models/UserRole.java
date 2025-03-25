package org.example.uthteammatching.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users_roles")
public class UserRole {
    @Id
    @Column(name = "maSo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maSo;
    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "maSo")
    private UthUser user;
    @ManyToOne
    @JoinColumn(name = "roleId",referencedColumnName = "maSo")
    private Role role;

    public UserRole() {

    }

    public UserRole(Long maSo, UthUser user, Role role) {
        this.maSo = maSo;
        this.user = user;
        this.role = role;
    }

    public Long getMaSo() {
        return maSo;
    }

    public void setMaSo(Long maSo) {
        this.maSo = maSo;
    }

    public UthUser getUser() {
        return user;
    }

    public void setUser(UthUser user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
