package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should return all customers"
    label "should return all customers"
    description """
        given:  repository contains 2 customers 
        when:   client requests list of customers
        then:   we will return list of 2 elements with 1 element of name Jane and id 1L
        """
    request {
        url "/api/customers"
        method GET()
        headers {
            contentType applicationJson()
        }
    }
    response {
        status(200)
        body([
                [id: 1L, name: "Jane"],
                [id: 2L, name: "Bob"]
        ])
        headers {
            contentType applicationJson()
        }
    }
}
