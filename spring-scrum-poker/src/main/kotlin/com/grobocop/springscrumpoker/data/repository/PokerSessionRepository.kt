package com.grobocop.springscrumpoker.data.repository

import com.grobocop.springscrumpoker.data.entity.PokerSession
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface PokerSessionRepository : CrudRepository<PokerSession, Int> {

    @Modifying
    @Transactional
    @Query("update PokerSession p set p.showEstimates = :showEstimates where p.id = :sessionId")
    fun updateSessionShowingState(@Param("showEstimates") showEstimate: Boolean,
                                  @Param("sessionId") sessionId: Int): Int

    @Query("select p.showEstimates from PokerSession p where p.id = :sessionId")
    fun getSessionShowingState(@Param("sessionId") sessionId: Int): Boolean
}