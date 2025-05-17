package uni.fmi.masters.talkify.model.common

data class CollectionModel<T>(
    val _embedded: Map<String, List<T>>
)
