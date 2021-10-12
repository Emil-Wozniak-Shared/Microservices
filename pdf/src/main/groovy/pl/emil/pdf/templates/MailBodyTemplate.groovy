package pl.emil.pdf.templates

import groovy.xml.MarkupBuilder

class MailBodyTemplate {
    private static def writer = new StringWriter()

    static String createTemplate(Map<String, Object> models) {
        def list = models.get("objects") as List<String>
        def html = new MarkupBuilder(writer)
        html.html {
            head {
                meta('http-equiv': "Content-Type", content: 'text/html; charset=iso-8859-1')
                style(type: 'text/css',
                        """
        .tg {
            border-collapse: collapse;
            border-color: #93a1a1;
            border-spacing: 0;
        }

        .tg td {
            background-color: #fdf6e3;
            border-color: #93a1a1;
            border-style: solid;
            border-width: 1px;
            color: #002b36;
            font-family: Arial, sans-serif;
            font-size: 14px;
            overflow: hidden;
            padding: 10px 5px;
            word-break: normal;
        }

        .tg th {
            background-color: #657b83;
            border-color: #93a1a1;
            border-style: solid;
            border-width: 1px;
            color: #fdf6e3;
            font-family: Arial, sans-serif;
            font-size: 14px;
            font-weight: normal;
            overflow: hidden;
            padding: 10px 5px;
            word-break: normal;
        }

        .tg .tg-0pky {
            border-color: inherit;
            text-align: left;
            vertical-align: top
        }
        """)
                title 'Constructed by MarkupBuilder'
            }
            body {
                div(style: 'direction: ltr;font-family: Tacoma;color: #000000;font-size: 10pt;') {
                    div(style: 'font-family: Times New Roman,serif; color: #000000; font-size: 16px') {
                        div {
                            div(dir: 'ltr') {
                                div('class': 'gmail_quote')
                                br()
                                br()
                                div {
                                    div(style: "clear:both; padding-top:4em") {
                                        div(style: "padding-top:2em", "Thank you for shopping at our company. ")
                                        div(style: "padding-top:1em", " Below you can find the attached details. ")
                                        div(style: "padding-top:1em", " Adobe Reader is required to view PDF files. This is a free\n" +
                                                "                                program available from the") {
                                            a(href: "https://get.adobe.com/reader/", target: "_blank", "Adobe website")
                                        }
                                        hr()
                                        div(style: "color:#808080; padding-top:2em; font-size:80%", "Have a nice day!")
                                    }
                                }
                                table('class': 'tg') {
                                    thead {
                                        tr {
                                            th()
                                            th("id")
                                            th("name")
                                        }
                                    }
                                    tbody {
                                        list.eachWithIndex { row, index ->
                                            tr {
                                                td()
                                                td(index)
                                                td(row)
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
        print writer.toString()
        return writer.toString()
    }
}
