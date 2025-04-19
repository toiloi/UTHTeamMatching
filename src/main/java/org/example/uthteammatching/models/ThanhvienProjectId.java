package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ThanhvienProjectId implements java.io.Serializable {
    private static final long serialVersionUID = -9093897019423483989L;

    @Column(name = "user_ma_so")
    private Long userMaSo;

    @Column(name = "project_ma_so")
    private Long projectMaSo;


    public ThanhvienProjectId() {}

    public ThanhvienProjectId(Long maSo, Long maProject) {
        this.userMaSo = maSo;
        this.projectMaSo = maProject;
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

    public void setMaProject(Long maProject) {

    }
}