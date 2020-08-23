package com.grobocop.springscrumpoker.controller

import com.grobocop.springscrumpoker.data.PokerSessionDTO
import com.grobocop.springscrumpoker.data.PokerSessionNotFound
import com.grobocop.springscrumpoker.data.UserEstimateDTO
import javassist.NotFoundException
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
    lateinit var pokerSessionService: PokerSessionService

    @GetMapping("/createNew")
    fun getNewSession(model: Model): String {
        val pokerSessionDTO = PokerSessionDTO()
        model.addAttribute("session",pokerSessionDTO)
        return "poker/createNew"
    }

    @PostMapping("/createNew")
    fun postNewSession(
            @ModelAttribute pokerSessionDTO: PokerSessionDTO,
            model: Model): String {
        if (pokerSessionDTO.name.isNotBlank() ) {
            val newSession = pokerSessionService.createSession(pokerSessionDTO)
            return "redirect:${newSession.id}"
        }
        return "redirect:createNew"
    }


    @GetMapping("/{id}")
    fun getSession(
            @PathVariable id: String,
            request: HttpServletRequest,
            model: Model): String {
        val readSession = pokerSessionService.readSession(id)
        readSession?.let{
            val username = request.cookies
                    ?.asList()
                    ?.stream()
                    ?.filter {
                        it.name == "username_$id"
                    }
                    ?.findFirst()
                    ?.get()
                    ?.value
            if (username.isNullOrBlank()) return "redirect:$id/name"
            model.addAttribute("id", id)
            return "poker/session"
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
            user.userName = user.userName.replace(' ','_')
            response.addCookie(Cookie("username_${id}", user.userName))
            return "redirect:/poker/${id}"
        }
        model.addAttribute("id", id)
        model.addAttribute("user", UserEstimateDTO())
        return "poker/name"
    }
}