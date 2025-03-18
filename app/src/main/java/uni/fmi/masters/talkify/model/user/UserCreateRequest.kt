package uni.fmi.masters.talkify.model.user

data class UserCreateRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
