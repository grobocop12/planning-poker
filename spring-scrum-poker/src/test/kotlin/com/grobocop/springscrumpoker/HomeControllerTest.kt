package com.grobocop.springscrumpoker

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private lateinit var mock : MockMvc

    @Test
    fun shouldReturnHomePage() {
        this.mock.perform(get("/")).andDo(print()).andExpect(status().isOk)
    }

}