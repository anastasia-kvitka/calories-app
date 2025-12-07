package com.example.caloriesapp.ai

data class ChatVisionResponse(
    val choices: List<VisionChoice>
)

data class VisionChoice(
    val index: Int,
    val message: VisionMessageContent
)

data class VisionMessageContent(
    val role: String,
    val content: String
)
