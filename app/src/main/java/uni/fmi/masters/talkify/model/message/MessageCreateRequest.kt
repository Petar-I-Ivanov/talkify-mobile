package uni.fmi.masters.talkify.model.message

data class MessageCreateRequest(
    val text: String,
    val channelId: String
)
