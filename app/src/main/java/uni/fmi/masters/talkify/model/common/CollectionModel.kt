package uni.fmi.masters.talkify.model.common

data class CollectionModel<T>(
    val embedded: Map<String, List<T>>
)
