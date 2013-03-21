package com.wireblend

import grails.test.ControllerUnitTestCase
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.mock.interceptor.MockFor
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.subject.Subject
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.SimpleSession
import org.apache.shiro.authc.UsernamePasswordToken
import org.junit.After

import static org.easymock.EasyMock.createMock
import static org.easymock.EasyMock.createNiceMock
import static org.easymock.EasyMock.createStrictMock
import static org.easymock.EasyMock.expect

@TestFor(AuthController)
@Mock([User,UserActivation])
class AuthControllerTests extends AbstractShiroTest {

    def user

    void setUp() {
        //1.  Create a mock authenticated Subject instance for the test to run:
        Subject subjectUnderTest = createNiceMock(Subject.class);
        expect(subjectUnderTest.isAuthenticated()).andReturn(true);

        //2. Bind the subject to the current thread:
        setSubject(subjectUnderTest);

        user = new User(
                username: 'test@gmail.com',
                passwordHash: new Sha256Hash("Password1").toHex(),
                userActivation: new UserActivation().save(flush: true, failOnError: true),
                locked: false
        ).save(flush: true, failOnError: true)
    }

    @After
    public void tearDownSubject() {
        //3. Unbind the subject from the current thread:
        clearSubject();
    }

    void test_IndexRedirect() {
        controller.index()
        assert response.redirectedUrl == "/auth/login"
    }

    void test_Login() {
        controller.login()
    }

    void test_LoginWithMessage() {
        params.message = "this is my message"
        controller.login()

        assert flash.message == "this is my message"
    }

    void test_UnauthorizedRender() {
        controller.unauthorized()
        def response = controller.response.contentAsString
        assert response == "You do not have permission to access this page."
    }

    void test_SignInWithLockedUser() {
        user.locked = true
        user.save(flush: true, failOnError: true)

        params.username = user.username
        params.password = "Password1"
        controller.signIn()

        assert response.redirectedUrl == "/auth/login?username="+URLEncoder.encode(user.username)+"&message=login.locked"
    }

    void test_SignInWithLockedUserAllParams() {
        user.locked = true
        user.save(flush: true, failOnError: true)

        params.targetUri = 'test'
        params.rememberMe = true
        params.username = user.username
        params.password = "Password1"
        controller.signIn()

        assert response.redirectedUrl == "/auth/login?username="+URLEncoder.encode(user.username)+"&message=login.locked&rememberMe=true&targetUri=test"
    }

    void test_SignIn() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: {} ] as Subject
        }

        params.username = user.username
        params.password = "Password1"
        controller.signIn()

        assert response.redirectedUrl == "/"
    }

    void test_SignInAfterFailure() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: {} ] as Subject
        }
        user.loginPeriodStartTimestamp = new Date()
        user.lastLoginAttemptTimestamp = new Date()
        user.loginAttemptCount = 1
        user.save(flush: true, failOnError: true)

        params.username = user.username
        params.password = "Password1"
        controller.signIn()

        assert response.redirectedUrl == "/"
        assert user.loginAttemptCount.equals(0)
    }

    void test_SignInWithAllParams() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: {} ] as Subject
        }

        params.targetUri = '/'
        params.rememberMe = true
        params.username = user.username
        params.password = "Password1"
        controller.signIn()

        assert response.redirectedUrl == "/"
    }

    void test_SignInBadUsername() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: { throw new AuthenticationException() } ] as Subject
        }

        params.username = "FakeUsername"
        params.password = "FakePassword"
        controller.signIn()

        assert response.redirectedUrl == "/auth/login?username=FakeUsername&message=login.failed"
    }

    void test_SignInBadPassword() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: { throw new AuthenticationException() } ] as Subject
        }

        params.username = user.username
        params.password = "FakePassword"
        controller.signIn()

        assert response.redirectedUrl == "/auth/login?username="+URLEncoder.encode(user.username)+"&message=login.failed"
        assert user.loginAttemptCount.equals(1)
    }

    void test_SignInBadPasswordAllParams() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: { throw new AuthenticationException() } ] as Subject
        }

        params.targetUri = 'badtest'
        params.rememberMe = true
        params.username = user.username
        params.password = "FakePassword"
        controller.signIn()

        assert response.redirectedUrl == "/auth/login?username="+URLEncoder.encode(user.username)+"&message=login.failed&rememberMe=true&targetUri=badtest"
        assert user.loginAttemptCount.equals(1)
    }

    void test_SignInBadPasswordFewTimes() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: { throw new AuthenticationException() } ] as Subject
        }
        user.loginPeriodStartTimestamp = new Date()
        user.lastLoginAttemptTimestamp = new Date()
        user.loginAttemptCount = 3
        user.save(flush: true, failOnError: true)

        params.username = user.username
        params.password = "FakePassword"
        controller.signIn()
        assert response.redirectedUrl == "/auth/login?username="+URLEncoder.encode(user.username)+"&message=login.failed"
        assert user.loginAttemptCount.equals(4)
    }

    void test_SignInBadPasswordExceeded() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: { throw new AuthenticationException() } ] as Subject
        }
        user.loginPeriodStartTimestamp = new Date() -1
        user.lastLoginAttemptTimestamp = new Date()
        user.loginAttemptCount = 5
        user.save(flush: true, failOnError: true)

        params.username = user.username
        params.password = "FakePassword"
        controller.signIn()
        assert response.redirectedUrl == "/auth/accessdenied"
        assert user.loginAttemptCount.equals(6)
    }

    void test_SignInBadPasswordExceededNoLongerBanned() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [login: { throw new AuthenticationException() } ] as Subject
        }
        user.loginPeriodStartTimestamp = new Date()
        user.lastLoginAttemptTimestamp = new Date()
        user.loginAttemptCount = 5
        user.save(flush: true, failOnError: true)

        params.username = user.username
        params.password = "FakePassword"
        controller.signIn()
        assert response.redirectedUrl == "/auth/accessdenied"
        assert user.loginAttemptCount.equals(6)
    }

    void test_SignOut() {
        SecurityUtils.metaClass.'static'.getSubject = { ->
            return [logout: { return true }, getPrincipal: { return "test" } ] as Subject
        }

        controller.signOut()
        assert response.redirectedUrl == "/"
    }

}
