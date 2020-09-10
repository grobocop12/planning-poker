package com.grobocop.springscrumpoker.data.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class PokerSession(
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
        var name: String = "",
        var showEstimates: Boolean = false,
        @OneToMany(
                cascade = [CascadeType.ALL],
                orphanRemoval = true,
                fetch = FetchType.EAGER
        )
        @JoinColumn(name = "poker_session_id")
        var userEstimates: List<UserEstimate> = emptyList(),
        @CreationTimestamp
        var created: Date = Date(),
        @UpdateTimestamp
        var modified: Date = Date()
){
        override fun toString(): String {
                return "PokerSession(id=$id, name='$name', showEstimates=$showEstimates, userEstimates=$userEstimates, created=$created, modified=$modified)"
        }
}
