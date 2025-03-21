package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;

import java.util.Objects;

@Embeddable
public class ThanhvienProjectId implements java.io.Serializable {
    private static final long serialVersionUID = -9093897019423483989L;
    @Nationalized
    @Column(name = "userMaSo", nullable = false, length = 50)
    private String userMaSo;

    @Nationalized
    @Column(name = "projectMaSo", nullable = false, length = 50)
    private String projectMaSo;

    public String getUserMaSo() {
        return userMaSo;
    }

    public void setUserMaSo(String userMaSo) {
        this.userMaSo = userMaSo;
    }

    public String getProjectMaSo() {
        return projectMaSo;
    }

    public void setProjectMaSo(String projectMaSo) {
        this.projectMaSo = projectMaSo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ThanhvienProjectId entity = (ThanhvienProjectId) o;
        return Objects.equals(this.userMaSo, entity.userMaSo) &&
                Objects.equals(this.projectMaSo, entity.projectMaSo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userMaSo, projectMaSo);
    }

}