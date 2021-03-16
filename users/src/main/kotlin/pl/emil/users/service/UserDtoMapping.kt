package pl.emil.users.service

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport
import org.modelmapper.PropertyMap
import org.springframework.stereotype.Component
import pl.emil.users.model.User
import pl.emil.users.model.UserDTO

@Component
class UserDtoMapping : PropertyMapConfigurerSupport<User, UserDTO>() {
    override fun mapping(): PropertyMap<User, UserDTO> =
        object : PropertyMap<User, UserDTO>() {
            override fun configure() {
                map().apply {
                    firstName = source.firstName
                    lastName = source.lastName
                    email = source.email
                }
            }
        }
}