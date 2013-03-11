package com.wireblend

import grails.test.ControllerUnitTestCase

class AuthControllerTests extends ControllerUnitTestCase {

    private static final String LOGGED_IN_USER_ID = 'authControllerUserId'
    private static final String LOGGED_IN_PASSWORD = 'authControllerUserPassword'
    private static final String REMEMBER_ME = false
    private static final String TARGET_URI = 'targetURI'

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testIndexRedirect() {
        controller.index()
        assert redirectArgs.action == "login"
    }

    void testUnauthorizedRender() {
        controller.unauthorized()
        def response = controller.response.contentAsString
        assert response == "You do not have permission to access this page."
    }

}
