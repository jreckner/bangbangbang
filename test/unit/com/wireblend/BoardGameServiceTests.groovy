package com.wireblend


import static grails.test.MockUtils.*
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BoardGameService)
@Mock([BoardGameGeekXmlApiService,UserService,User,BoardGame,UserActivation])
class BoardGameServiceTests {

    def testBoardGame
    def user

    void setUp() {
        def mockedBoardGameSearchResults = []
        mockedBoardGameSearchResults.add(new BoardGameDTO(name: 'Dominion0', objectId: '1'))
        mockedBoardGameSearchResults.add(new BoardGameDTO(name: 'Dominion1', objectId: '2'))
        testBoardGame = new BoardGame(
                objectId: '123456',
                name: 'MyBoardGame',
                age: 21,
                minPlayers: 2,
                maxPlayers: 6,
                description: 'long description here',
                yearPublished: 2013,
                image: 'http://someUrlHere',
                thumbnail: 'http//someUrlhere/t',
                playingTime: 90
        )
        testBoardGame.validate()
        testBoardGame.save(flush: true, failonerror: true)

        def mockedBoardGameGeekXmlApiService = [
                searchBoardGameGeek: { searchString -> mockedBoardGameSearchResults },
                searchBoardGameGeekExact: { searchString -> mockedBoardGameSearchResults },
                getBoardGameGeekDetailsById: { gameid -> testBoardGame }
        ] as BoardGameGeekXmlApiService
        service.boardGameGeekXmlApiService = mockedBoardGameGeekXmlApiService

        user = new User(
                username: 'test1@gmail.com',
                passwordHash: "hashedPassword1",
                userActivation: new UserActivation().save(flush: true))
        user.save(flush: true, failonerror: true)
        def mockedUserService = [getUser: { username -> user }]
        service.userService = mockedUserService

        mockLogging(BoardGameGeekXmlApiService, true)
    }

    void test_SearchGamesByName() {
        def boardGameSearchResults =  service.searchGamesByName('MyBoardGame')
        assert boardGameSearchResults[0].name.equals('MyBoardGame')
        assert boardGameSearchResults[0].objectId.equals('123456')
        assert boardGameSearchResults[0].yearPublished.equals(2013)
        assert boardGameSearchResults[0].playingTime.equals(90)
    }

    void test_SearchGamesByNameNotFound() {
        def mockedBoardGameGeekXmlApiService = [
                searchBoardGameGeek: { searchString -> null }
        ]
        service.boardGameGeekXmlApiService = mockedBoardGameGeekXmlApiService

        def boardGameSearchResults =  service.searchGamesByName('FakeGame')
        assert boardGameSearchResults.size().equals(0)
    }

    void test_SearchGamesByNameNotExact() {
        def boardGameSearchResults =  service.searchGamesByName('MyBoardGame',false)
        assert boardGameSearchResults[0].name.equals('MyBoardGame')
        assert boardGameSearchResults[0].objectId.equals('123456')
        assert boardGameSearchResults[0].yearPublished.equals(2013)
        assert boardGameSearchResults[0].playingTime.equals(90)
    }

    void test_SearchGamesByExactName() {
        def boardGameSearchResults =  service.searchGamesByName('Dominion',true)
        assert boardGameSearchResults[0].name.equals('MyBoardGame')
        assert boardGameSearchResults[0].objectId.equals('123456')
        assert boardGameSearchResults[0].yearPublished.equals(2013)
        assert boardGameSearchResults[0].playingTime.equals(90)
    }

    void test_GetGameDetails() {
        def boardGame = service.getGameDetails('123456')
        assert boardGame.objectId.equals('123456')
        assert boardGame.name.equals('MyBoardGame')
    }

    void test_AddToUserCollection() {
        service.addToUserCollection('test1@gmail.com', testBoardGame)
        assert user.boardGames.size().equals(1)
    }

    void test_AddToUserCollectionByUsername() {
        service.addToUserCollection(user.username, testBoardGame)
        assert user.boardGames.size().equals(1)
    }

    void test_AddToUserCollectionByUser() {
        service.addToUserCollection(user, testBoardGame)
        assert user.boardGames.size().equals(1)
    }

    void test_AddToUserCollectionByUserInvalid() {
        def mockedUserService = [getUser: { username -> null }]
        service.userService = mockedUserService
        service.addToUserCollection('fakeUserName', testBoardGame)
    }

    void test_RemoveFromUserCollection() {
        service.addToUserCollection('test1@gmail.com', testBoardGame)
        assert user.boardGames.size().equals(1)

        service.removeFromUserCollection('test1@gmail.com', testBoardGame)
        assert user.boardGames.size().equals(0)
    }

    void test_RemoveFromUserCollectionByUsername() {
        service.addToUserCollection(user.username, testBoardGame)
        assert user.boardGames.size().equals(1)

        service.removeFromUserCollection(user.username, testBoardGame)
        assert user.boardGames.size().equals(0)
    }

    void test_RemoveFromUserCollectionByUser() {
        service.addToUserCollection(user, testBoardGame)
        assert user.boardGames.size().equals(1)

        service.removeFromUserCollection(user, testBoardGame)
        assert user.boardGames.size().equals(0)
    }

    void test_RemoveFromUserCollectionByUserInvalid() {
        def mockedUserService = [getUser: { username -> null }]
        service.userService = mockedUserService
        service.removeFromUserCollection('fakeUserName', testBoardGame)
    }

    void test_findAll() {
        def boardGames = service.findAll()
        assert boardGames.size().equals(1)
    }

    void test_createBoardGame() {
        def boardGame = new BoardGameDTO()
        boardGame.objectId = "123456789"
        boardGame.name = 'primaryName'
        boardGame.age = Integer.parseInt('21')
        boardGame.minPlayers = Integer.parseInt('2')
        boardGame.maxPlayers = Integer.parseInt('6')
        boardGame.description = 'Long Description'
        boardGame.yearPublished = Integer.parseInt('2014')
        boardGame.image = 'imageUrl'
        boardGame.thumbnail = 'thumbnailUrl'
        boardGame.playingTime = Integer.parseInt('45')

        service.createBoardGame(boardGame)
        assert BoardGame.findByObjectId("123456789")
    }
}
