package com.kstd.talk.entity;

import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.TalkState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@IdClass(TalkMemId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalkMem {

    @Id
    @Column(name = "TALK_ID")
    private String talkId;

    @Id
    @Column(name = "MEM_ID")
    private String memId;

    @Column(name = "STATE")
    private TalkState state;

    @Column(name = "DTM",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dtm;

    @Column(name = "UPDATE_DTM",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDtm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TALK_ID", insertable = false, updatable = false)
    private TalkList talkList;

    public TalkMem(TalkMemberDto memberDto) {
        this.talkId = memberDto.getTalkId();
        this.memId = memberDto.getMemId();
        this.state = memberDto.getState();
    }
}
