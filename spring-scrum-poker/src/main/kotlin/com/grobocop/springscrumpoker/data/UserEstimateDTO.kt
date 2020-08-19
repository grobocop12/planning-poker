package com.grobocop.springscrumpoker.data

import com.grobocop.springscrumpoker.data.entity.UserEstimate

data class UserEstimateDTO(var userName: String = "", var estimate : Int? = null) {
    constructor(userEstimate: UserEstimate) : this(userEstimate.username, userEstimate.estimate)
}