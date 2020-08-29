package com.grobocop.springscrumpoker

import com.grobocop.springscrumpoker.controller.PokerSessionService
import com.grobocop.springscrumpoker.data.PokerSessionDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PokerSessionServiceTest {

    @Autowired
    private lateinit var service: PokerSessionService

    private var id: String? = null


    @Test
    fun runTests() {
        createSessionTest()
        readSessionTest()
        updateSessionTest()
        deleteSessionTest()
    }

    private fun createSessionTest() {
        val name = "session"
        val showEstimates = false
        val sessionDTO = PokerSessionDTO(
                id = "",
                name = name,
                showEstimates = showEstimates,
                userEstimates = ArrayList()
        )
        val createSession = service.createSession(sessionDTO)
        this.id = createSession.id
        assert(createSession.name == name)
        assert(createSession.showEstimates == showEstimates)
        assert(createSession.id.matches("^[0-9]{10}$".toRegex()))
    }

    private fun readSessionTest() {
        id?.let {
            val let = service.getSession(it)
            assert(let?.id == it)
        }
    }

    private fun updateSessionTest() {
        val name = "anotherName"
        val showEstimates = true
        id?.let {
            val dto = PokerSessionDTO(
                    id = it,
                    name = name,
                    showEstimates = showEstimates
            )
            val result = service.updateSession(dto)
            assert(result.id == dto.id)
            assert(result.name == dto.name)
            assert(result.showEstimates == dto.showEstimates)
        }
    }

    private fun deleteSessionTest() {
        id?.let {
            service.deleteSession(it)
            service.getSession(it)?.let {
                assert(false)
            }
            assert(true)
        }
    }

}