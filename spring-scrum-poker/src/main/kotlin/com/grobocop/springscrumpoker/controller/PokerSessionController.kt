package com.grobocop.springscrumpoker.controller

import com.grobocop.springscrumpoker.data.PokerSessionDTO
import com.grobocop.springscrumpoker.data.PokerSessionNotFound
import com.grobocop.springscrumpoker.data.UserEstimateDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/poker")
class PokerSessionController {

    @Autowired
    private lateinit var pokerSessionService: PokerSessionService

    //expires after 31 days
    private val cookieExpirationTime: Int = 60 * 60 * 24 * 31
    private val cookiePath = "/poker"

    @GetMapping("/createNew")
    fun getNewSession(model: Model): String {
        val pokerSessionDTO = PokerSessionDTO()
        model.addAttribute("session", pokerSessionDTO)
        return "poker/createNew"
    }

    @PostMapping("/createNew")
    fun postNewSession(
            @ModelAttribute pokerSessionDTO: PokerSessionDTO,
            model: Model): String {
        if (pokerSessionDTO.name.isNotBlank()) {
            val newSession = pokerSessionService.createSession(pokerSessionDTO)
            return "redirect:${newSession.id}"
        }
        return "redirect:createNew"
    }


    @GetMapping("/{id}")
    fun getSession(
            @PathVariable id: String,
            request: HttpServletRequest,
            response: HttpServletResponse,
            model: Model): String {
        val readSession = pokerSessionService.getSession(id)
        readSession?.let {
            var usernameCookie: Cookie? = null
            var userIdCookie: Cookie? = null
            val cookies = request.cookies?.asList()
            val optionalUsernameCookie = cookies
                    ?.stream()
                    ?.filter { cookie -> cookie.name == "username_$id" }
                    ?.findFirst()
            val optionalUserIdCookie = cookies
                    ?.stream()
                    ?.filter { cookie -> cookie.name == "user_id_${id}" }
                    ?.findFirst()
            if (optionalUsernameCookie?.isPresent == true) usernameCookie = optionalUsernameCookie.get()
            if (optionalUserIdCookie?.isPresent == true) userIdCookie = optionalUserIdCookie.get()
            if (userIdCookie == null || usernameCookie == null ||
                    usernameCookie.value.isNullOrBlank() || userIdCookie.value.isNullOrBlank()) {
                return "redirect:$id/name"
            } else if (it.userEstimates.any { element ->
                        element.id.toString() == userIdCookie.value && element.userName == usernameCookie.value
                    }) {
                resetCookie(response, usernameCookie, usernameCookie.value)
                resetCookie(response, userIdCookie, userIdCookie.value)
                model.addAttribute("id", id)
                model.addAttribute("pokerSession", it)
                return "poker/session"
            } else {
                val newUser = pokerSessionService.addUserToSession(id, UserEstimateDTO(userName = usernameCookie.value))
                resetCookie(response, usernameCookie, newUser.userName)
                resetCookie(response, userIdCookie, newUser.id)
                model.addAttribute("id", id)
                model.addAttribute("pokerSession", it)
                return "poker/session"
            }
        }
        throw PokerSessionNotFound()
    }

    @GetMapping("/{id}/name")
    fun getName(
            @PathVariable id: String,
            response: HttpServletResponse,
            model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("user", UserEstimateDTO())
        return "poker/name"
    }

    @PostMapping("/{id}/name")
    fun postName(
            @PathVariable id: String,
            @ModelAttribute user: UserEstimateDTO,
            response: HttpServletResponse,
            model: Model): String {
        if (user.userName.isNotBlank()) {
            user.userName = user.userName.replace(' ', '_')
            val addedUser = pokerSessionService.addUserToSession(id, user)
            addCookie(response, "username_${id}", addedUser.userName)
            addCookie(response, "user_id_${id}", addedUser.id.toString())
            return "redirect:/poker/${id}"
        }
        model.addAttribute("id", id)
        model.addAttribute("user", UserEstimateDTO())
        return "poker/name"
    }

    private fun addCookie(response: HttpServletResponse, cookieName: String, value: Any) {
        val cookie = Cookie(cookieName, value.toString())
        cookie.path = cookiePath
        cookie.maxAge = cookieExpirationTime
        response.addCookie(cookie)
    }

    private fun resetCookie(response: HttpServletResponse, cookie: Cookie, newValue: Any) {
        val cookieName = cookie.name
        cookie.maxAge = 0
        response.addCookie(cookie)
        val newCookie = Cookie(cookieName, newValue.toString())
        newCookie.maxAge = cookieExpirationTime
        newCookie.path = cookiePath
        response.addCookie(newCookie)
    }
}