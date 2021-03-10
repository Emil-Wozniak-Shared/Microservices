import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import static ch.qos.logback.classic.Level.*

conversionRule("clr", ColorConverter)
conversionRule("wex", WhitespaceThrowableProxyConverter)
conversionRule("wEx", ExtendedWhitespaceThrowableProxyConverter)

def PID = System.getProperty("PID") ?: ''
def CONSOLE_LOG_PATTERN =
        "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} " +
                "%clr(%5p) " +
                "%clr(${PID}){magenta} " +
                "%clr(---){faint} %clr([%15.15t]){faint} " +
                "%clr(%-40.40logger{39}){cyan} " +
                "%clr(:){faint} %m%n%wEx"

appender("CONSOLE", ConsoleAppender) {
    withJansi = true
    encoder(PatternLayoutEncoder) {
        pattern = "${CONSOLE_LOG_PATTERN}"
    }
}

root(INFO, ["CONSOLE"])

// Below appended loggers
logger("javax.activation", WARN)
logger("javax.mail", WARN)
logger("javax.management.remote", WARN)
logger("javax.xml.bind", WARN)
logger("ch.qos.logback", WARN)
logger("com.hazelcast", INFO)
logger("com.ryantenney", WARN)
logger("com.sun", WARN)
logger("com.zaxxer", WARN)
logger("io.undertow", WARN)
logger("io.undertow.websockets.jsr", ERROR)
logger("org.apache", WARN)
logger("org.apache.kafka", INFO)
logger("org.apache.catalina.startup.DigesterFactory", OFF)
logger("org.bson", WARN)
logger("org.hibernate.validator", WARN)
logger("org.hibernate", WARN)
logger("org.hibernate.ejb.HibernatePersistence", OFF)
logger("org.postgresql", WARN)
logger("org.springframework", WARN)
logger("org.springframework.web", WARN)
logger("org.springframework.security", WARN)
logger("org.springframework.cache", WARN)
logger("org.thymeleaf", WARN)
logger("org.xnio", WARN)
logger("springfox", WARN)
logger("sun.rmi", WARN)
logger("liquibase", INFO)
logger("org.liquibase.logging.Logger", INFO)
logger("org.liquibase", INFO)
logger("LiquibaseSchemaResolver", INFO)
logger("springfox.documentation.schema.property", ERROR)
logger("sun.net.www", INFO)
logger("sun.rmi.transport", WARN)