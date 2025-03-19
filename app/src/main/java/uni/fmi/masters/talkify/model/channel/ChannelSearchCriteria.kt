package uni.fmi.masters.talkify.model.channel

data class ChannelSearchCriteria(
    val name: String,
    val userId: String,
    val ownerId: String,
    val adminId: String,
    val guestId: String,
    val active: Boolean
)
