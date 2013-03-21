package com.wireblend

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.shiro.crypto.hash.Sha256Hash

@TestFor(User)
@Mock(UserActivation)
class UserTests {

    def user

    void setUp() {
        user = new User(
            username: 'test@gmail.com',
            passwordHash: new Sha256Hash("Password1").toHex(),
            userActivation: new UserActivation().save(flush: true)).save()
    }

    void test_CreateUser() {
        assert User.findByUsername('test@gmail.com')
    }

    void test_IncrementLoginAttemptCount() {
        assert user.getLoginAttemptCount().equals(0)
        user.incrementLoginAttemptCount()
        assert user.getLoginAttemptCount().equals(1)
        user.incrementLoginAttemptCount()
        assert user.getLoginAttemptCount().equals(2)
    }

    void test_ResetLoginAttemptCount() {
        user.incrementLoginAttemptCount()
        assert user.getLoginAttemptCount().equals(1)
        user.resetLoginAttemptCount()
        assert user.getLoginAttemptCount().equals(0)
    }

    void test_ToString() {
        assert user.toString() == "$user"
    }
}
