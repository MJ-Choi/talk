package com.kstd.talk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TalkIdSeqRepository {

    private final EntityManager em;

    @Transactional
    public Integer createTalkId() {

        // INSERT 쿼리 실행
        Query insertQuery = em.createNativeQuery("INSERT INTO TALK_ID_SEQ VALUES (NULL)");
        insertQuery.executeUpdate();

        // SELECT LAST_INSERT_ID() 쿼리 실행
        Query selectQuery = em.createNativeQuery("SELECT LAST_INSERT_ID()");
        return ((Number) selectQuery.getSingleResult()).intValue();
    }
}