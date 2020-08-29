package com.grobocop.springscrumpoker.data

import com.grobocop.springscrumpoker.data.entity.UserEstimate

data class UserEstimateDTO(var id: Int = 0, var userName: String = "", var estimate: Int? = null) {
    constructor(userEstimate: UserEstimate) : this(userEstimate.id, userEstimate.username, userEstimate.estimate)
}