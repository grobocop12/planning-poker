package com.grobocop.springscrumpoker.data

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Session Not Found")
class PokerSessionNotFound: RuntimeException() {
}