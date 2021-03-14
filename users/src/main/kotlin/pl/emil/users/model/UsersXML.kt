package pl.emil.users.model

import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "users")
@XmlAccessorType(FIELD)
data class UsersXML(
    @XmlElement(name = "user")
    var users: List<User> = mutableListOf()
) {

}