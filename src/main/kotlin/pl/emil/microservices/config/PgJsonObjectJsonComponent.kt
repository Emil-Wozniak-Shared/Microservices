package pl.emil.microservices.config

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.r2dbc.postgresql.codec.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
@Suppress("unused")
internal class PgJsonObjectJsonComponent {

    class Deserializer : JsonDeserializer<Json>() {
        private val log: Logger = getLogger(this::class.java.simpleName)

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Json =
            ctxt.readTree(p).let {
                log.info("read json value :{}", it)
                Json.of(it.toString())
            }
    }

    class Serializer : JsonSerializer<Json>() {
        private val log: Logger = getLogger(this::class.java.simpleName)

        override fun serialize(
            value: Json,
            gen: JsonGenerator,
            serializers: SerializerProvider
        ) {
            val text: String = value.asString()
            log.info("The raw json value from PostgresSQL JSON type:{}", text)
            val factory = JsonFactory()
            val parser = factory.createParser(text)
            val node = gen.codec.readTree<TreeNode>(parser)
            serializers.defaultSerializeValue(node, gen)
        }
    }
}