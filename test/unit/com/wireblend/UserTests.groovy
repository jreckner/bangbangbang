package com.wireblend

import grails.test.mixin.TestFor
import org.apache.shiro.crypto.hash.Sha256Hash
import grails.test.GrailsUnitTestCase

@TestFor(User)
class UserTests {

    void test_CreateUser() {
        def user = new User(
                username: 'test@gmail.com',
                passwordHash: new Sha256Hash("Password1").toHex(),
                activationKey: UUID.randomUUID() as String)
        user.save()

        assert User.findByUsername('test@gmail.com')
    }
}
