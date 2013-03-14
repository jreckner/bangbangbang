package com.wireblend

import grails.test.ControllerUnitTestCase
import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationController)
@Mock([User,UserActivation])
class RegistrationControllerTests{

    def user

    void setUp() {
        user = new User(
                username: 'test@gmail.com',
                passwordHash: new Sha256Hash("Password1").toHex(),
                userActivation: new UserActivation().save(flush:  true))
        user.save()

        def mockedRegistrationService = [
                activate: { uuid -> true },
                register: { username,password,baseUrl -> true }
        ] as RegistrationService
        controller.registrationService = mockedRegistrationService

        def mockedUserService = [
                getUser: { username -> null }
        ] as UserService
        controller.userService = mockedUserService
    }

    void tearDown() {
    }

    void test_IndexRedirect() {
        controller.index()
    }

    void test_ActivationCompleteRedirect() {
        controller.activationComplete()
    }

    void test_registrationCompleteRedirect() {
        controller.registrationComplete()
    }

    void test_registrationFailureRedirect() {
        controller.registrationFailure()
    }

    void test_Activate() {
        params.activationKey = UUID.randomUUID()
        controller.activate()
        assert response.redirectedUrl == '/registration/activationComplete'
    }

    void test_ActivateInvalid() {
        def mockedRegistrationService = [activate: {uuid -> false}]
        controller.registrationService = mockedRegistrationService

        params.activationKey = UUID.randomUUID()
        controller.activate()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }

    void test_Register() {
        params.username = 'test@example.com'
        params.password = 'matchThis'
        params.password2 = 'matchThis'
        controller.register()
        assert response.redirectedUrl == '/registration/registrationComplete'
    }

    void test_RegisterPasswordMismatch() {
        params.username = 'test@example.com'
        params.password = 'matchThis'
        params.password2 = 'mismatched'
        controller.register()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }

    void test_RegisterExistingUser() {
        def mockedUserService = [
                getUser: { username -> user }
        ] as UserService
        controller.userService = mockedUserService

        params.username = 'test@example.com'
        params.password = 'matchThis'
        params.password2 = 'matchThis'
        controller.register()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }

    void test_RegisterUnableToRegister() {
        def mockedRegistrationService = [
                register: { username,password,baseUrl -> false }
        ] as RegistrationService
        controller.registrationService = mockedRegistrationService

        params.username = 'test@example.com'
        params.password = 'matchThis'
        params.password2 = 'matchThis'
        controller.register()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }

    /*void test_RegisterBlankParams() {
        params.username = ''
        params.password = ''
        params.password2 = ''
        controller.register()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }

    void test_RegisterNullParams() {
        params.username = null
        params.password = null
        params.password2 = null
        controller.register()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }*/
}
