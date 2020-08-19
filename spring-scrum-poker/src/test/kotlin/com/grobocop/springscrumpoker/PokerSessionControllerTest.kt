package com.grobocop.springscrumpoker

import com.grobocop.springscrumpoker.data.UserEstimateDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc
class PokerSessionControllerTest {

    @Autowired
    private lateinit var mock: MockMvc

    private val sessionId = "session"


    @Test
    fun getSessionWithCookieTest() {
        this.mock
                .perform(get("/poker/${sessionId}")
                        .cookie(Cookie("username_${sessionId}", "pioter")))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(model().attribute("id", sessionId))
    }

    @Test
    fun getSessionWithBlankCookieTest() {
        this.mock
                .perform(get("/poker/${sessionId}")
                        .cookie(Cookie("username_${sessionId}", "           ")))
                .andExpect(redirectedUrl("${sessionId}/name"))
                .andExpect(status().isFound)
    }

    @Test
    fun getSessionWithoutCookieTest() {
        this.mock
                .perform(get("/poker/${sessionId}"))
                .andDo(print())
                .andExpect(redirectedUrl("${sessionId}/name"))
                .andExpect(status().isFound)
    }

    @Test
    fun getNameTest() {
        this.mock
                .perform(get("/poker/${sessionId}/name"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(model().attribute("user", UserEstimateDTO()))
    }

    @Test
    fun postNameTest() {
        val name = "exampleName"
        this.mock
                .perform(post("/poker/${sessionId}/name")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", name)
                )
                .andExpect(redirectedUrl("/poker/${sessionId}"))
                .andExpect(status().isFound)
    }

    @Test
    fun postBlankNameTest() {
        this.mock
                .perform(post("/poker/${sessionId}/name")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", "")
                )
                .andExpect(status().isOk)
                .andExpect(model().attribute("id", sessionId))
                .andExpect(model().attribute("user", UserEstimateDTO()))
    }
}