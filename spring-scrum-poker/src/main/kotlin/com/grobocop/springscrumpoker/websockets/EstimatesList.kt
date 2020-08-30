package com.grobocop.springscrumpoker.websockets

import com.grobocop.springscrumpoker.data.UserEstimateDTO

class EstimatesList(val showEstimates: Boolean, val estimates: List<UserEstimateDTO>) {
}