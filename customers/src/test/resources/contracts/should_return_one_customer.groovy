package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "get one customers by id"
    description """
        given:  repository contains 2 customers 
        when:   client requests one customer with id 1
        then:   we will return one customer with 1 element of name Jane and id 1L
        """
    request {
        url "/customers/2"
        method GET()
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status(200)
        body(file("json/response.json"))
        headers {
            contentType(applicationJson())
        }
    }
}
