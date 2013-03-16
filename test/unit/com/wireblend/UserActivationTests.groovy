package com.wireblend



import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserActivation)
class UserActivationTests {

    void test_ActivationKeyIsValid() {
        def userActivation = new UserActivation().save(flush: true)

        assert userActivation.activationKey.length().equals(36)
        assert userActivation.activationStartDate.after(new Date() - 1) // Newer then yesterday
    }
}
