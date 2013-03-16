package com.wireblend



import grails.test.mixin.*
import org.junit.*
import grails.converters.JSON

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(BoardGameController)
@Mock([BoardGameService,BoardGame,UserService,User,UserActivation])
class BoardGameControllerTests {

    def user
    def boardGame

    void setUp() {
        def boardGames = []
        def boardGameDTO = new BoardGameDTO(name: 'TestGame', objectId: '123456')
        boardGames.add(boardGameDTO)

        boardGame = new BoardGame(
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
        boardGame.validate()
        boardGame.save(flush: true, failOnError: true)

        def allBoardGames = []
        allBoardGames.add(boardGame)
        def mockedBoardGameService = [
                searchGamesByName: { searchKeyword -> boardGames },
                searchGamesByName: { searchKeyword,exact -> boardGames },
                getGameDetails: { objectId -> boardGame },
                addToUserCollection: { username,boardGame -> null },
                removeFromUserCollection: { username,boardGame -> null },
                findAll: { return allBoardGames }
        ] as BoardGameService
        controller.boardGameService = mockedBoardGameService

        user = new User(
                username: 'test1@gmail.com',
                passwordHash: "hashedPassword1",
                userActivation: new UserActivation().save(flush:  true))
        user.validate()
        user.addToBoardGames(boardGame)
        user.save(flush: true, failonerror: true)

        def mockedUserService = [
                getUser: { username -> user }
        ] as UserService
        controller.userService = mockedUserService
    }

    void test_SearchBoardGames() {
        params.exact = 1
        params.sEcho = 1
        params.searchKeyword = 'testGame'
        controller.searchBoardGames()

        def text = controller.response.contentAsString
        def json = JSON.parse(text)

        assert json.sEcho.equals(1)
        assert json.iTotalRecords.equals(1)
        assert json.iTotalDisplayRecords.equals(1)
    }

    void test_SearchBoardGamesNotExact() {
        params.exact = 0
        params.sEcho = 2
        params.searchKeyword = 'testGame'
        controller.searchBoardGames()

        def text = controller.response.contentAsString
        def json = JSON.parse(text)

        assert json.sEcho.equals(2)
        assert json.iTotalRecords.equals(1)
        assert json.iTotalDisplayRecords.equals(1)
    }

    void test_AssignBoardGameToUser() {
        params.username = user.username
        params.boardGameObjectId = boardGame.objectId
        controller.assignBoardGameToUser()

        // need to return something from the controller to test we did it!
    }

    void test_RemoveBoardGameFromUser() {
        params.username = user.username
        params.boardGameObjectId = boardGame.objectId
        controller.removeBoardGameFromUser()

        // need to return something from the controller to test we did it!
    }

    void test_GetBoardGameDetails() {
        params.objectId = boardGame.objectId
        controller.getBoardGameDetails()

        def text = controller.response.contentAsString
        def json = JSON.parse(text)

        assert json.objectId.equals(boardGame.objectId)
        assert json.name.equals(boardGame.name)
    }

    void test_GetBoardGameDetailsBadId() {
        def mockedBoardGameService = [
                getGameDetails: { objectId -> null }
        ] as BoardGameService
        controller.boardGameService = mockedBoardGameService

        params.objectId = boardGame.objectId
        controller.getBoardGameDetails()

        def text = controller.response.contentAsString

        assert text.length().equals(0)
    }

    void test_GetAllBoardGames() {
        params.sEcho = 5
        controller.getAllBoardGames()

        def text = controller.response.contentAsString
        def json = JSON.parse(text)

        assert json.sEcho.equals(5)
        assert json.iTotalRecords.equals(1)
        assert json.iTotalDisplayRecords.equals(1)
    }

    void test_GetAllBoardGamesForUser() {
        params.sEcho = 6
        params.username = user.username
        controller.getAllBoardGamesForUser()

        def text = controller.response.contentAsString
        def json = JSON.parse(text)

        assert json.sEcho.equals(6)
        assert json.iTotalRecords.equals(1)
        assert json.iTotalDisplayRecords.equals(1)
    }
}
