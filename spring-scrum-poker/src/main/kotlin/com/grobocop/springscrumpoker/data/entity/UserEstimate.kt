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
               generator = "user_estimate_generator"
       )
       @GenericGenerator(
               name = "user_estimate_generator",
               strategy = "native"
       )
       var id: Int = 0,
       @NotBlank
       var username: String = "",
       var estimate: String? = null,
       @ManyToOne(fetch = FetchType.LAZY)
       var pokerSession: PokerSession? = null,
       @CreationTimestamp
       var created: Date = Date(),
       @UpdateTimestamp
       var modified: Date = Date()
){
       override fun toString(): String {
              return "UserEstimate(id=$id, username='$username', estimate=$estimate, created=$created, modified=$modified)"
       }
}