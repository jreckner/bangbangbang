package com.wireblend

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.shiro.crypto.hash.Sha256Hash

@TestFor(Role)
class RoleTests {

    def role

    void setUp() {
        // Create the user role
        role = new Role(name: 'ROLE_USER').save(flush: true, failOnError: true)
    }

    void test_CreateRole() {
        assert Role.findByName('ROLE_USER')
    }

    void test_ToString() {
        assert role.toString() == "$role"
    }
}
