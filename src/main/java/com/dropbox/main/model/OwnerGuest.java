package com.dropbox.main.model;

import javax.persistence.*;

@Entity
@Table(name = "owner_guest")
public class OwnerGuest {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "entity_id_seq"
    )
    @SequenceGenerator(
            name = "entity_id_seq",
            sequenceName = "global_id_sequence",
            allocationSize = 1
    )
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "file_id")
    private int fileId;

    @Column(name = "guest_id")
    private int guestId;

    @Column(name = "access")
    private boolean access;

}