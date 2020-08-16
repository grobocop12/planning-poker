package com.grobocop.springscrumpoker.controller

import com.grobocop.springscrumpoker.data.UserDTO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/poker")
class PokerSessionController {

    @GetMapping("/{id}")
    fun getSession(
            @PathVariable id: String,
            request: HttpServletRequest,
            model: Model): String {
        val username = request.cookies
                ?.asList()
                ?.stream()
                ?.filter {
                    it.name == "username_${id}"
                }
                ?.findFirst()
                ?.get()
                ?.value
        if (username.isNullOrBlank()) {
            return "redirect:${id}/name"
        }
        model.addAttribute("id", id)
        return "poker/session"
    }

    @GetMapping("/{id}/name")
    fun getName(
            @PathVariable id: String,
            response: HttpServletResponse,
            model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("user", UserDTO())
        return "poker/name"
    }

    @PostMapping("/{id}/name")
    fun postName(
            @PathVariable id: String,
            @ModelAttribute user: UserDTO,
            response: HttpServletResponse,
            model: Model): String {
        if (user.userName.isNotBlank()) {
            response.addCookie(Cookie("username_${id}", user.userName))
            return "redirect:/poker/${id}"
        }
        model.addAttribute("id", id)
        model.addAttribute("user", UserDTO())
        return "poker/name"
    }
}