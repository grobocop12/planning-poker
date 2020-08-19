package com.grobocop.springscrumpoker.controller

import com.grobocop.springscrumpoker.data.PokerSessionDTO

interface PokerSessionService {
    fun createSession(session: PokerSessionDTO) : PokerSessionDTO
    fun readSession(id: String) : PokerSessionDTO
    fun updateSession(dto: PokerSessionDTO): PokerSessionDTO
    fun deleteSession(id: String)
}