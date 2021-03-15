package pl.emil.users.model

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "users")
data class UsersXML(
    @XmlElement
    var users: MutableList<User> = mutableListOf()
)