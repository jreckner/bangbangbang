package com.wireblend



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BoardGameGeekXmlApiService)
@Mock(BoardGame)
class BoardGameGeekXmlApiServiceTests {

    void setUp() {
    }

    void test_SearchBoardGameGeekExact() {
        service.searchBoardGameGeekExact('Dominion')
    }

    void test_GetBoardGameGeekDetailsById() {
        def boardGame = service.getBoardGameGeekDetailsById('36218') // This is for Dominion
        assert boardGame
    }
}
