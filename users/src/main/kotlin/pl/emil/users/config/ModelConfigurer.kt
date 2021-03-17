package pl.emil.users.config

import com.github.jmnarloch.spring.boot.modelmapper.ModelMapperConfigurer
import org.modelmapper.ModelMapper
import org.modelmapper.convention.NamingConventions.NONE
import org.springframework.stereotype.Component

@Component
class ModelConfigurer : ModelMapperConfigurer {

    override fun configure(modelMapper: ModelMapper) {
        modelMapper
            .configuration
            .sourceNamingConvention = NONE
    }
}
