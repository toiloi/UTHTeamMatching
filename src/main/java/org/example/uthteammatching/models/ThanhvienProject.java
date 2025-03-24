package org.example.uthteammatching.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class ThanhvienProject {
    @EmbeddedId
    private ThanhvienProjectId id;

    @MapsId("userMaSo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userMaSo", nullable = false)
    private org.example.uthteammatching.models.UthUser userMaSo;

    @MapsId("projectMaSo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "projectMaSo", nullable = false)
    private Project projectMaSo;

    @Nationalized
    @Column(name = "vaiTro", length = 50)
    private String vaiTro;

    public ThanhvienProjectId getId() {
        return id;
    }

    public void setId(ThanhvienProjectId id) {
        this.id = id;
    }

    public org.example.uthteammatching.models.UthUser getUserMaSo() {
        return userMaSo;
    }

    public void setUserMaSo(org.example.uthteammatching.models.UthUser userMaSo) {
        this.userMaSo = userMaSo;
    }

    public Project getProjectMaSo() {
        return projectMaSo;
    }

    public void setProjectMaSo(Project projectMaSo) {
        this.projectMaSo = projectMaSo;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

}