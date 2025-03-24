package org.example.uthteammatching.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class GiangVien {
    @Id
    @Nationalized
    @Column(name = "maSo", nullable = false, length = 50)
    private String maSo;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "maSo", nullable = false)
    private org.example.uthteammatching.models.UthUser uthUser;

    public String getMaSo() {
        return maSo;
    }

    public void setMaSo(String maSo) {
        this.maSo = maSo;
    }

    public org.example.uthteammatching.models.UthUser getUthUser() {
        return uthUser;
    }

    public void setUthUser(org.example.uthteammatching.models.UthUser uthUser) {
        this.uthUser = uthUser;
    }

}