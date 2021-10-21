package pl.emil.pdf.templates

import groovy.xml.MarkupBuilder
import org.springframework.core.io.ClassPathResource
import pl.emil.pdf.dto.NameDto
import pl.emil.pdf.dto.UserDto

import java.nio.file.Files
import java.nio.file.Path

class MailBodyTemplate {
    private static def writer = new StringWriter()

    static String createTemplate(Map<String, Object> models) {
        def names = models.get("objects") as List<NameDto>
        def users = models.get("users") as List<UserDto>
        def html = new MarkupBuilder(writer)
        def customStyle = Files.readAllLines(Path.of(new ClassPathResource("styles/user-table.css").file.toURI()))
        def dStyles = Files.readAllLines(Path.of(new ClassPathResource("styles/default-table.css").file.toURI()))
        html.html {
            head {
                meta('http-equiv': "Content-Type", content: 'text/html; charset=iso-8859-1')
                //noinspection CssUnknownProperty
                style(type: 'text/css', """
               table {
                    -fs-table-paginate: paginate;
                    border-spacing: 0;
                }
                ${dStyles.join("\n")}
                
                ${customStyle.join("\n")}
        
                """
                )
                title 'Constructed by MarkupBuilder'
            }
            body {
                div(style: 'font-family: Tacoma;color: #000000;font-size: 10pt;') {
                    div(style: 'font-family: Times New Roman,serif; color: #000000; font-size: 16px') {
                        div {
                            div {
                                div {
                                    div(style: "clear:both; padding-top:4em") {
                                        h1(style: "padding-top:2em", "Thank you for shopping at our company. ")
                                        h2(style: "padding-top:1em", " Below you can find the attached details. ")
                                        div(style: "padding-top:1em", " Adobe Reader is required to view PDF files. This is a free\n" +
                                                "                                program available from the") {
                                            a(href: "https://get.adobe.com/reader/", target: "_blank", "Adobe website")
                                        }
                                        hr(style: 'width: 200px;')
                                        div(style: "color:#808080; padding-top:2em; font-size:80%", "Have a nice day!")
                                    }
                                }
                                br()
                                p(style: 'font-size: 12px;', "paragraph 12")
                                p(style: 'font-size: 14px;', "paragraph 14")
                                br()
                                table('class': 'customTable') {
                                    thead {
                                        tr {
                                            th("id")
                                            th("first name")
                                            th("last name")
                                            th("email")
                                            th("karma")
                                            th("created at")
                                        }
                                    }
                                    tbody {
                                        users.eachWithIndex { user, index ->
                                            tr {
                                                td(index)
                                                td(user.firstName)
                                                td(user.lastName)
                                                td(user.email)
                                                td(user.karma)
                                                td(user.createdAt)
                                            }
                                        }
                                    }
                                }
                                hr()
                                br()
                                table('class': 'tg') {
                                    thead {
                                        tr {
                                            th("id")
                                            th("surname")
                                            th("approximate")
                                            th("number")
                                            th("freq.")
                                            th("letters")
                                        }
                                    }
                                    tbody {
                                        names.eachWithIndex { row, index ->
                                            tr {
                                                td(index)
                                                td(row.surname)
                                                td(row.approximate)
                                                td(row.number)
                                                td(row.frequency)
                                                td(row.surname.length())
                                            }
                                        }
                                    }
                                }
                                br()
                            }
                        }
                    }
                }

            }
        }
        return writer.toString()
    }
}
