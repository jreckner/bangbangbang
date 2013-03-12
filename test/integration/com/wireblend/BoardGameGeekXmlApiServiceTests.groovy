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

    void test_SearchBoardGameGeek() {
        def boardGameDTOList = service.searchBoardGameGeek('Dominion')
        assert boardGameDTOList.size().equals(26)
    }

    void test_SearchBoardGameGeekExact() {
        def boardGameDTOList = service.searchBoardGameGeekExact('Dominion')
        assert boardGameDTOList[0].objectId.equals('36218')
    }

    void test_GetBoardGameGeekDetailsById() {
        def boardGame = service.getBoardGameGeekDetailsById('36218') // This is for Dominion
        assert boardGame
        assert boardGame.objectId.equals('36218')
    }
}
