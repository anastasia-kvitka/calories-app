package com.example.caloriesapp.ai

data class ChatVisionRequest(
    val model: String,
    val messages: List<VisionMessage>
)

data class VisionMessage(
    val role: String,
    val content: List<VisionContent>
)

sealed class VisionContent {
    data class Text(val type: String = "text", val text: String) : VisionContent()
    data class ImageUrl(
        val type: String = "image_url",
        val image_url: VisionImageURL
    ) : VisionContent()
}

data class VisionImageURL(
    val url: String
)
