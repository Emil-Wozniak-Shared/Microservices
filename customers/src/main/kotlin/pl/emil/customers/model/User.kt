package pl.emil.customers.model

data class User(
    var id: Long = 0,
    var firstname: String? = null,
    var lastname: String? = null,
    var age: Int = 0,
)
