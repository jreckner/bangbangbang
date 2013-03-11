package com.wireblend



import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import java.util.UUID

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationService)
@Mock(User)
class RegistrationServiceTests {

    def user
    def activationKey

    void setUp() {
        activationKey = UUID.randomUUID() as String
        user = new User(
                username: 'test@gmail.com',
                passwordHash: new Sha256Hash("Password1").toHex(),
                activationKey: activationKey)
        user.save(flush: true, failonerror: true)

        def mockedMailService = [sendMail: { return null }]
        service.mailService = mockedMailService
    }

    void test_Activate() {
        assert service.activate(activationKey)
    }

    void test_ActivateFail() {
        assert !service.activate("badKeyString")
    }

    void test_GetNewActivationKey() {
        assert service.getNewActivationKey().length().equals(36)
    }

    void test_SendActivationEmail() {
        service.sendActivationEmail('trash@example.com', activationKey, 'FAKEHTMLURLLink')
    }
}
