package com.grobocop.springscrumpoker.data.repository

import com.grobocop.springscrumpoker.data.entity.UserEstimate
import org.springframework.data.jpa.repository.JpaRepository

interface UserEstimateRepository: JpaRepository<UserEstimate, Int> {
}