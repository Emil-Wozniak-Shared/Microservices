package pl.emil.users.service

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport
import org.modelmapper.AbstractConverter
import org.modelmapper.Converter
import org.springframework.stereotype.Component
import pl.emil.users.model.User
import pl.emil.users.model.UserDTO

@Component
class UserDtoConverter : ConverterConfigurerSupport<User, UserDTO>() {
    override fun converter(): Converter<User, UserDTO> =
        object : AbstractConverter<User, UserDTO>() {
            override fun convert(source: User): UserDTO =
                source.run {
                    UserDTO(firstName, lastName, email)
                }
        }
}