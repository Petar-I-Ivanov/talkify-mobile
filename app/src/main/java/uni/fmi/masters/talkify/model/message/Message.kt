package uni.fmi.masters.talkify.model.message

import uni.fmi.masters.talkify.model.common.Link
import java.time.LocalDateTime

data class Message(
    val id: String,
    val text: String,
    val sender: String,
    val sentAt: String,
    val editedAt: String?,
    val _links: Map<String, Link>
)
