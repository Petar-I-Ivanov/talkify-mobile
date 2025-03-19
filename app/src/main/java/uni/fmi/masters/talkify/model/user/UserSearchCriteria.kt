package uni.fmi.masters.talkify.model.user

data class UserSearchCriteria (
    val search: String,
    val username: String,
    val email: String,
    val inChannelId: String,
    val notInChannelId: String,
    val onlyFriends: Boolean,
    val active: Boolean,
    val page: Number,
    val size: Number,
    val sort: String
)