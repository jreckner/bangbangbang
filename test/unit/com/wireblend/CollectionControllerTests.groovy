package com.wireblend

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CollectionController)
class CollectionControllerTests {

    void testIndexRedirect() {
        controller.index()
        assert response.status == 200
    }
}
