package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class ThanhvienProject {
    @EmbeddedId
    private ThanhvienProjectId id;

    @MapsId("userMaSo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_ma_so", nullable = false)
    private org.example.uthteammatching.models.UthUser userMaSo;

    @MapsId("projectMaSo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_ma_so", nullable = false)
    private Project projectMaSo;

    @Nationalized
    @Column(name = "vai_tro", length = 50)
    private String vaiTro;

}