package com.wireblend



import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.grails.ShiroSecurityService
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User,Role,UserActivation])
class UserServiceTests {

    def user
    def mockedShiroSecurityService

    void setUp() {

        // Shiro adds some metaClass methods that make unit testing tricky!
        User.metaClass.static.addToRoles = { role -> null }

        mockedShiroSecurityService = [encodePassword: { String password -> password }] as ShiroSecurityService
        service.shiroSecurityService = mockedShiroSecurityService

        user = new User(
                username: 'test1@gmail.com',
                passwordHash: "hashedPassword1",
                serActivation: new UserActivation().save(flush: true))
        user.save(flush: true, failonerror: true)
    }

    void test_getUser() {
        assert service.getUser('test1@gmail.com')
    }

    void test_createLockedUser() {
        def user = service.createLockedUser('test2@gmail.com', 'password1')
        assert user.locked
    }

    void test_createLockedUserPasswordToShortCriteria() {
        def user = service.createLockedUser('test3@gmail.com', 'PW1')
        assert !user
    }

    /*void test_createLockedUserPasswordNeedsNumeric() {
        def user = service.createLockedUser('test3@gmail.com', 'password', UUID.randomUUID() as String)
        assert !user
    }

    void test_createLockedUserPasswordNeedsUpperCase() {
        def user = service.createLockedUser('test3@gmail.com', 'password1', UUID.randomUUID() as String)
        assert !user
    }

    void test_createLockedUserPasswordNeedsLowerCase() {
        def user = service.createLockedUser('test3@gmail.com', 'PASSWORD1', UUID.randomUUID() as String)
        assert !user
    }*/
}
