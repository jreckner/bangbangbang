package com.wireblend

import grails.test.ControllerUnitTestCase
import grails.test.mixin.*
import org.apache.shiro.SecurityUtils
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.subject.Subject
import org.junit.*

import javax.swing.AbstractAction
import java.net.URLEncoder

import static org.easymock.EasyMock.createNiceMock
import static org.easymock.EasyMock.expect

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationController)
@Mock([User,UserActivation])
class RegistrationControllerTests extends AbstractShiroTest {

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

        // i18n mockup
        controller.metaClass.message = { it.code }
    }

    void tearDown() {
    }

    void test_IndexRedirect() {

        //1.  Create a mock authenticated Subject instance for the test to run:
        Subject subjectUnderTest = createNiceMock(Subject.class);
        expect(subjectUnderTest.isAuthenticated()).andReturn(true);

        //2. Bind the subject to the current thread:
        setSubject(subjectUnderTest);

        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: {}, isAuthenticated: { return true }, getPrincipal: { return user.username } ] as Subject
        }

        def mockedUserService = [
                getUser: { username -> user }
        ] as UserService
        controller.userService = mockedUserService

        controller.index()
        assert response.redirectedUrl == "/user/show/"+user.id
    }

    void test_IndexRedirectNotLoggedIn() {

        //1.  Create a mock authenticated Subject instance for the test to run:
        Subject subjectUnderTest = createNiceMock(Subject.class);
        expect(subjectUnderTest.isAuthenticated()).andReturn(true);

        //2. Bind the subject to the current thread:
        setSubject(subjectUnderTest);

        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: {}, isAuthenticated: { return false }, getPrincipal: { return null } ] as Subject
        }

        controller.index()
        assert response.status == 200
    }

    void test_registrationCompleteRedirect() {
        controller.registrationComplete()
    }

    void test_Activate() {
        params.activationKey = UUID.randomUUID()
        params.username = user.username
        controller.activate()
        assert response.redirectedUrl == "/auth/login?username="+URLEncoder.encode(user.username)+"&message=useraccount+is+activated+and+unlocked."
    }

    void test_ActivateInvalid() {
        def mockedRegistrationService = [activate: {uuid -> false}]
        controller.registrationService = mockedRegistrationService

        params.activationKey = UUID.randomUUID()
        params.username = user.username
        controller.activate()
        assert flash.message == "unable to find username '${user.username}' for activation."
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
        params.username = user.username
        params.password = 'matchThis'
        params.password2 = 'mismatched'
        controller.register()
        assert flash.message == "Passwords do not match"
        assert response.redirectedUrl == "/registration/index?username="+URLEncoder.encode(user.username)
    }

    void test_RegisterExistingUser() {
        def mockedUserService = [
                getUser: { username -> user }
        ] as UserService
        controller.userService = mockedUserService

        params.username = user.username
        params.password = 'matchThis'
        params.password2 = 'matchThis'
        controller.register()
        assert flash.message == "User already exists with the username '${user.username}'"
        assert response.redirectedUrl == '/registration/index'
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
        assert response.redirectedUrl == '/registration/index'
    }

}
