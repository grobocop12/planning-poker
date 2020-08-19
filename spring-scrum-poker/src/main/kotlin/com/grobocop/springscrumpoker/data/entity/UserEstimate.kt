package com.grobocop.springscrumpoker.data.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class UserEstimate(
       @Id
       @GeneratedValue(
               strategy = GenerationType.AUTO,
               generator = "native"
       )
       @GenericGenerator(
               name = "native",
               strategy = "native"
       )
       var id: Int = 0,
       @NotBlank
       var username: String = "",
       var estimate: Int? = 0,
       @ManyToOne(fetch = FetchType.LAZY)
       var pokerSession: PokerSession? = null,
       @CreationTimestamp
       var created: Date = Date(),
       @UpdateTimestamp
       var modified: Date = Date()
) {
}