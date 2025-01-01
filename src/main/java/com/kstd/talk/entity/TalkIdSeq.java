package com.kstd.talk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "TALK_ID_SEQ")
public class TalkIdSeq {

    @Id
    @Column(name = "id")
    private int id;
}
