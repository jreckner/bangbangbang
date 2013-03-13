package com.wireblend

import grails.plugin.mail.MailService

import static grails.test.MockUtils.*
import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import java.util.UUID

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationService)
@Mock([User, MailService, UserService, UserActivation])
class RegistrationServiceTests {

    def user

    void setUp() {
        user = new User(
                username: 'test@gmail.com',
                passwordHash: new Sha256Hash("Password1").toHex(),
                userActivation: new UserActivation().save(flush: true))
        user.save(flush: true, failonerror: true)

        def mockedUserService = [
                createLockedUser: { username,password -> user },
        ] as UserService
        service.userService = mockedUserService

        def mockedMailService = [sendMail: { def closure -> return null }]
        service.mailService = mockedMailService
    }

    void test_Activate() {
        assert service.activate(user.userActivation.activationKey)
    }

    void test_ActivateFail() {
        assert !service.activate("badKeyString")
    }

    void test_SendActivationEmail() {
        service.sendActivationEmail('trash@example.com', user.userActivation.activationKey, 'FAKEHTMLURLLink')
    }

    void test_Register() {
        assert service.register('trash@example.com', 'Password1', 'FAKEHTMLURLLink')
    }

    void test_UnableToRegister() {
        def mockedUserService = [
                createLockedUser: { username,password -> null },
        ] as UserService
        service.userService = mockedUserService

        assert !service.register('trash@example.com', 'Password1', 'FAKEHTMLURLLink')
    }
}
