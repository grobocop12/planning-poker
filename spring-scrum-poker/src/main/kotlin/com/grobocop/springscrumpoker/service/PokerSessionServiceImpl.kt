package com.grobocop.springscrumpoker.service

import com.grobocop.springscrumpoker.controller.PokerSessionService
import com.grobocop.springscrumpoker.data.PokerSessionDTO
import com.grobocop.springscrumpoker.data.PokerSessionNotFound
import com.grobocop.springscrumpoker.data.UserEstimateDTO
import com.grobocop.springscrumpoker.data.entity.PokerSession
import com.grobocop.springscrumpoker.data.repository.PokerSessionRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PokerSessionServiceImpl : PokerSessionService {

    @Autowired
    private lateinit var repository: PokerSessionRepository

    override fun createSession(session: PokerSessionDTO): PokerSessionDTO {
        val result = repository.save(
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
                }
        )
    }

    override fun readSession(id: String): PokerSessionDTO? {
        try {
            val idInt = Integer.parseInt(id)
            val sessionOptional = repository.findById(idInt)
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
                        }
                )
            }
        } catch (e: NumberFormatException) {
            return null
        }

    }

    override fun updateSession(dto: PokerSessionDTO): PokerSessionDTO {
        val idInt = dto.id.toInt()
        val findById = repository.findById(idInt)
        if (findById.isEmpty) {
            throw PokerSessionNotFound()
        } else {
            val toSave = findById.get()
            toSave.name = dto.name
            toSave.showEstimates = dto.showEstimates
            toSave.modified = Date()
            val result = repository.save(toSave)
            return PokerSessionDTO(
                    result.id.toString().padStart(10, '0'),
                    result.name,
                    result.showEstimates,
                    result.userEstimates.map {
                        UserEstimateDTO(it)
                    }
            )
        }
    }

    override fun deleteSession(id: String) {
        val idInt = id.toInt()
        val findById = repository.findById(idInt)
        if (findById.isEmpty) {
            throw PokerSessionNotFound()
        } else {
            repository.deleteById(idInt)
        }
    }
}