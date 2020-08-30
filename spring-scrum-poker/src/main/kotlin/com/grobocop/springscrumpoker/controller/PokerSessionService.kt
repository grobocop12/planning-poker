package com.grobocop.springscrumpoker.controller

import com.grobocop.springscrumpoker.data.PokerSessionDTO
import com.grobocop.springscrumpoker.data.UserEstimateDTO

interface PokerSessionService {
    fun createSession(session: PokerSessionDTO) : PokerSessionDTO
    fun getSession(id: String) : PokerSessionDTO?
    fun updateSession(dto: PokerSessionDTO): PokerSessionDTO
    fun deleteSession(id: String)
    fun addUserToSession(id: String, user: UserEstimateDTO) : UserEstimateDTO
    fun setSessionShowingState(roomId: String, state: Boolean) : Boolean
}