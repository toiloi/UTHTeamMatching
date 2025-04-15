package org.example.uthteammatching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ListFriendId implements java.io.Serializable {
    private static final long serialVersionUID = 7560222654797851229L;
    @NotNull
    @Column(name = "user_id_1", nullable = false)
    private Long userId1;

    @NotNull
    @Column(name = "user_id_2", nullable = false)
    private Long userId2;

    public ListFriendId(Long userId1, Long userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public ListFriendId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ListFriendId entity = (ListFriendId) o;
        return Objects.equals(this.userId1, entity.userId1) &&
                Objects.equals(this.userId2, entity.userId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId1, userId2);
    }

}