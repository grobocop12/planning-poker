package com.grobocop.springscrumpoker.service

import com.grobocop.springscrumpoker.controller.PokerSessionService
import com.grobocop.springscrumpoker.data.PokerSessionDTO
import com.grobocop.springscrumpoker.data.PokerSessionNotFound
import com.grobocop.springscrumpoker.data.UserEstimateDTO
import com.grobocop.springscrumpoker.data.entity.PokerSession
import com.grobocop.springscrumpoker.data.entity.UserEstimate
import com.grobocop.springscrumpoker.data.repository.PokerSessionRepository
import com.grobocop.springscrumpoker.data.repository.UserEstimateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PokerSessionServiceImpl : PokerSessionService {

    @Autowired
    private lateinit var pokerSessionRepository: PokerSessionRepository

    @Autowired
    private lateinit var userEstimateRepository: UserEstimateRepository

    override fun createSession(session: PokerSessionDTO): PokerSessionDTO {
        val result = pokerSessionRepository.save(
                PokerSession(
                        name = session.name,
                        showEstimates = session.showEstimates,
                        userEstimates = emptyList()
                )
        )
        return PokerSessionDTO(
                id = result.id.toString().padStart(10, '0'),
                name = result.name,
                showEstimates = result.showEstimates,
                userEstimates = result.userEstimates.map {
                    UserEstimateDTO(it)
                }.toMutableList()
        )
    }

    override fun getSession(id: String): PokerSessionDTO? {
        try {
            val idInt = Integer.parseInt(id)
            val sessionOptional = pokerSessionRepository.findById(idInt)
            return if (sessionOptional.isEmpty) {
                null
            } else {
                val result = sessionOptional.get()
                PokerSessionDTO(
                        id = result.id.toString().padStart(10, '0'),
                        name = result.name,
                        showEstimates = result.showEstimates,
                        userEstimates = result.userEstimates.map {
                            UserEstimateDTO(it)
                        }.toMutableList()
                )
            }
        } catch (e: NumberFormatException) {
            return null
        }

    }

    override fun updateSession(dto: PokerSessionDTO): PokerSessionDTO {
        val idInt = dto.id.toInt()
        val findById = pokerSessionRepository.findById(idInt)
        if (findById.isEmpty) {
            throw PokerSessionNotFound()
        } else {
            val toSave = findById.get()
            toSave.name = dto.name
            toSave.showEstimates = dto.showEstimates
            toSave.modified = Date()
            toSave.userEstimates = dto.userEstimates.map {
                UserEstimate(
                        username = it.userName,
                        pokerSession = toSave
                )
            }
            val result = pokerSessionRepository.save(toSave)
            return PokerSessionDTO(
                    result.id.toString().padStart(10, '0'),
                    result.name,
                    result.showEstimates,
                    result.userEstimates.map {
                        UserEstimateDTO(it)
                    }.toMutableList()
            )
        }
    }

    override fun deleteSession(id: String) {
        val idInt = id.toInt()
        val findById = pokerSessionRepository.findById(idInt)
        if (findById.isEmpty) {
            throw PokerSessionNotFound()
        } else {
            pokerSessionRepository.deleteById(idInt)
        }
    }

    override fun addUserToSession(id: String, user: UserEstimateDTO): UserEstimateDTO {
        val idInt = id.toInt()
        val findById = pokerSessionRepository.findById(idInt)
        if (findById.isEmpty) {
            throw PokerSessionNotFound()
        } else {
            val get = findById.get()
            val userEstimate = UserEstimate(
                    username = user.userName,
                    pokerSession = get)
            return UserEstimateDTO(userEstimateRepository.save(userEstimate))
        }
    }

    override fun setSessionShowingState(roomId: String, state: Boolean): Boolean {
        pokerSessionRepository.updateSessionShowingState(state, roomId.toInt())
        return pokerSessionRepository.getSessionShowingState(roomId.toInt())
    }
}