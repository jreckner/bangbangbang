package com.wireblend



import grails.test.mixin.*
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.grails.ShiroSecurityService
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User,Role])
class UserServiceTests {

    def user
    def activationKey
    def mockedShiroSecurityService

    void setUp() {

        // Shiro adds some metaClass methods that make unit testing tricky!
        User.metaClass.static.addToRoles = { role -> null }

        mockedShiroSecurityService = [encodePassword: { String password -> password }] as ShiroSecurityService
        service.shiroSecurityService = mockedShiroSecurityService

        activationKey = UUID.randomUUID() as String
        user = new User(
                username: 'test1@gmail.com',
                passwordHash: "hashedPassword1",
                activationKey: activationKey)
        user.save(flush: true, failonerror: true)
    }

    void test_getUser() {
        assert service.getUser('test1@gmail.com')
    }

    void test_createLockeduser() {
        def user = service.createLockedUser('test2@gmail.com', 'password1', UUID.randomUUID() as String)
        assert user.locked
    }
}
