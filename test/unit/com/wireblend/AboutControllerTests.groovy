package com.wireblend



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AboutController)
class AboutControllerTests {

    void testIndexRedirect() {
        controller.index()
    }
}
