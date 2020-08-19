package com.grobocop.springscrumpoker.data.repository

import com.grobocop.springscrumpoker.data.entity.PokerSession
import org.springframework.data.repository.CrudRepository

interface PokerSessionRepository : CrudRepository<PokerSession, Int> {
}