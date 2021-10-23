package pl.emil.pdf

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.core.io.ClassPathResource
import pl.emil.pdf.handler.hasText
import com.codeborne.pdftest.PDF as PDF1

class RealWorldExamplesTest : FeatureSpec({
    feature("File should contains") {
        scenario("bank statements") {
            val pdf = PDF1(ClassPathResource("statement.pdf").url)
            with(pdf) {
                hasText("Выписка") shouldBe true
                hasText("Период: 18.06.2015 - 18.06.2015") shouldBe true
                hasText("Счёт: тудашный") shouldBe true
                hasText("Входящий остаток на 18.06.2015 6.40 RUB") shouldBe true
                hasText("Дата Плательщик / Получатель Операция Сумма (RUB)") shouldBe true
                hasText("18.06.2015 Solntsev Andrei \t 40820810590550000146 \t сюда. Интернет-банк -1.00") shouldBe true
                hasText("18.06.2015 Solntsev Andrei \t 40820810590550000146 \t туда. Интернет-банк 1.00") shouldBe true
                hasText("Исходящий остаток на 18.06.2015 6.40 RUB") shouldBe true
                hasText("Поступление 1.00 RUB") shouldBe true
                hasText("Списание -1.00 RUB") shouldBe true
            }
        }
        scenario("bank transaction") {
            val pdf = PDF1(ClassPathResource("transaction.pdf").inputStream)
            pdf.run {
                hasText("24.06.2015") shouldBe true
                hasText("Поступ. в банк плат.") shouldBe true
                hasText("Списано со сч. плат.") shouldBe true
                hasText("0401060") shouldBe true
                hasText("ПЛАТЕЖНОЕ ПОРУЧЕНИЕ № 338") shouldBe true
                hasText("Дата") shouldBe true
                hasText("Вид платежа") shouldBe true
                hasText("Сумма прописью") shouldBe true
                hasText("Один рубль 00 копеек") shouldBe true
                hasText("ИНН") shouldBe true
                hasText("КПП") shouldBe true
                hasText("Solntsev Andrei") shouldBe true
                hasText("Плательщик") shouldBe true
                hasText("Сч.№") shouldBe true
                hasText("40820810590550000146") shouldBe true
                hasText("ПАО \"БАНК \"САНКТ-ПЕТЕРБУРГ\"") shouldBe true
                hasText("Отметки банка") shouldBe true
                hasText("БИК 044030790") shouldBe true
                hasText("Исполнен 24.06.2015") shouldBe true
                hasText("Код операции 169765333614") shouldBe true
            }
        }
    }
})
