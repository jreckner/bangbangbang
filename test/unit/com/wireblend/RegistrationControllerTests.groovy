package com.wireblend

import grails.test.ControllerUnitTestCase
import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationController)
@Mock(User)
class RegistrationControllerTests{

    def user

    void setUp() {
        user = new User(
                username: 'test@gmail.com',
                passwordHash: new Sha256Hash("Password1").toHex(),
                activationKey: UUID.randomUUID() as String)
        user.save()

        def mockedRegistrationService = [
                activate: { uuid -> true },
                sendActivationEmail: { username,activationKey,link -> null },
                getNewActivationKey: { return UUID.randomUUID() }
        ] as RegistrationService
        controller.registrationService = mockedRegistrationService

        def mockedUserService = [
                getUser: { username -> null },
                createLockedUser: { username,password,uuid -> true }
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

    void test_RegisterUnableToCreateLockedUser() {
        def mockedUserService = [
                getUser: { username -> null },
                createLockedUser: { username,password,uuid -> false }
        ] as UserService
        controller.userService = mockedUserService

        params.username = 'test@example.com'
        params.password = 'matchThis'
        params.password2 = 'matchThis'
        controller.register()
        assert response.redirectedUrl == '/registration/registrationFailure'
    }
}
