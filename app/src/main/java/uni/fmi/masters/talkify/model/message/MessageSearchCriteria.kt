package uni.fmi.masters.talkify.model.message

data class MessageSearchCriteria(
    val channelId: String,
    val page: Number,
    val size: Number,
    val sort: String
)
