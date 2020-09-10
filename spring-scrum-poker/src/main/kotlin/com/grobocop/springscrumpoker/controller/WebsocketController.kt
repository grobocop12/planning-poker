package com.grobocop.springscrumpoker.controller

import com.grobocop.springscrumpoker.data.UserEstimateDTO
import com.grobocop.springscrumpoker.websockets.EstimatesList
import com.grobocop.springscrumpoker.websockets.ShowingState
import com.grobocop.springscrumpoker.websockets.Vote
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse

@Controller
class WebsocketController {

    private val logger: Logger = LoggerFactory.getLogger(WebsocketController::class.java)

    @Autowired
    private lateinit var service: PokerSessionService

    @MessageMapping("/poker/{roomId}")
    @SendTo("/broker/poker/{roomId}/listOfUsers")
    fun newConnection(@Payload user: UserEstimateDTO,
                      @DestinationVariable roomId: String): EstimatesList? {
        val readSession = service.getSession(roomId)
        readSession?.let {
            return EstimatesList(it.showEstimates, it.userEstimates.toList())
        }
        return null
    }

    @MessageMapping("/poker/{roomId}/show")
    @SendTo("/broker/poker/{roomId}/show")
    fun showEstimates(@Payload showingState: ShowingState,
                      @DestinationVariable roomId: String): ShowingState {
        val state = service.setSessionShowingState(roomId, showingState.state)
        return ShowingState(state)
    }

    @MessageMapping("/poker/{roomId}/vote")
    @SendTo("/broker/poker/{roomId}/vote")
    fun vote(@Payload vote: Vote,
             @DestinationVariable roomId: String): UserEstimateDTO {
        val updateEstimate = service.updateEstimate(vote.userId, vote.userEstimate);
        return updateEstimate
    }

    @MessageMapping("/poker/{roomId}/delete")
    @SendTo("/broker/poker/{roomId}/listOfUsers")
    fun deleteEstimates(@DestinationVariable roomId: String): EstimatesList? {
        val deleteEstimates = service.deleteEstimates(roomId)
        return deleteEstimates?.let {
            return EstimatesList(it.showEstimates, it.userEstimates.toList())
        }
    }
}