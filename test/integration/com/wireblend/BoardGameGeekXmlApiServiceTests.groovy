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

    void test_SearchBoardGameGeekDominion() {
        def boardGameDTOList = service.searchBoardGameGeek('Dominion')
        assert boardGameDTOList.size().equals(27)
    }

    void test_SearchBoardGameGeekExact() {
        def boardGameDTOList = service.searchBoardGameGeekExact('Dominion')
        assert boardGameDTOList[0].objectId.equals('36218')
    }

    void test_GetBoardGameGeekDetailsByIdDieMacher() {
        def boardGame = service.getBoardGameGeekDetailsById('1') // This is for Dominion
        assert boardGame
        assert boardGame.objectId.equals('1')
    }

    void test_GetBoardGameGeekDetailsByIdDominion() {
        def boardGame = service.getBoardGameGeekDetailsById('36218') // This is for Dominion
        assert boardGame
        assert boardGame.objectId.equals('36218')
    }

    void test_GetBoardGameGeekDetailsByIdTicketToRide() {
        def boardGame = service.getBoardGameGeekDetailsById('9209') // This is for Ticket to Ride
        assert boardGame
        assert boardGame.objectId.equals('9209')
    }
}
