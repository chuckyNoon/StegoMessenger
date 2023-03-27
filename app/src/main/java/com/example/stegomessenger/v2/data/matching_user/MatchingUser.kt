package com.example.stegomessenger.v2.data.matching_user

import com.example.core.network.model.MatchingUserDTO

data class MatchingUser(
    val id: String,
    val name: String
){
    companion object{
        fun fromDTO(dto: MatchingUserDTO) : MatchingUser =
            MatchingUser(
                id = dto.id,
                name = dto.name
            )
    }
}