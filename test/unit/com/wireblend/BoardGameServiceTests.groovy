package com.wireblend


import static grails.test.MockUtils.*
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BoardGameService)
@Mock([BoardGameGeekXmlApiService,UserService,User,BoardGame])
class BoardGameServiceTests {

    def testBoardGame
    def user, activationKey

    void setUp() {
        def mockedBoardGameSearchResults = []
        mockedBoardGameSearchResults.add(new BoardGameDTO(name: 'Dominion0', objectId: '1'))
        mockedBoardGameSearchResults.add(new BoardGameDTO(name: 'Dominion1', objectId: '2'))
        testBoardGame = new BoardGame(
                objectId: '123456',
                name: 'MyFakeGame',
                age: 21,
                minPlayers: 2,
                maxPlayers: 6,
                description: 'long description here',
                yearPublished: 2013,
                image: 'http://someUrlHere',
                thumbnail: 'http//someUrlhere/t',
                playingTime: 90
        )
        def mockedBoardGameGeekXmlApiService = [
                searchBoardGameGeek: { searchString -> mockedBoardGameSearchResults },
                searchBoardGameGeekExact: { searchString -> mockedBoardGameSearchResults },
                getBoardGameGeekDetailsById: { gameid -> testBoardGame }
        ] as BoardGameGeekXmlApiService
        service.boardGameGeekXmlApiService = mockedBoardGameGeekXmlApiService

        activationKey = UUID.randomUUID() as String
        user = new User(
                username: 'test1@gmail.com',
                passwordHash: "hashedPassword1",
                activationKey: activationKey)
        user.save(flush: true, failonerror: true)
        def mockedUserService = [getUser: { username -> user }]
        service.userService = mockedUserService

        mockLogging(BoardGameGeekXmlApiService, true)
    }

    void test_SearchGamesByName() {
        def boardGameSearchResults =  service.searchGamesByName('Dominion')
        assert boardGameSearchResults[0].name.equals('Dominion0')
        assert boardGameSearchResults[0].objectId.equals('1')
        assert boardGameSearchResults[1].name.equals('Dominion1')
        assert boardGameSearchResults[1].objectId.equals('2')
    }

    void test_SearchGamesByExactName() {
        def boardGameSearchResults =  service.searchGamesByExactName('Dominion')
        assert boardGameSearchResults[0].name.equals('Dominion0')
        assert boardGameSearchResults[1].name.equals('Dominion1')
    }

    void test_GetGameDetails() {
        def boardGame = service.getGameDetails('123456')
        assert boardGame.objectId.equals('123456')
        assert boardGame.name.equals('MyFakeGame')
    }

    void test_AddToUserCollection() {
        //def boardGame = service.addToUserCollection('test1@gmail.com', testBoardGame)
    }
}
