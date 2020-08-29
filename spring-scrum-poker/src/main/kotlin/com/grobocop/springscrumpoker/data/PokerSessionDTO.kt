package com.grobocop.springscrumpoker.data

class PokerSessionDTO(
        var id : String = "",
        var name: String = "",
        var showEstimates : Boolean = false,
        var userEstimates : MutableList<UserEstimateDTO> = ArrayList()
)