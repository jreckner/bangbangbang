package com.wireblend

import com.wireblend.BoardGame
import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(BoardGame)
class BoardGameTests {

    void test_CreateBoardGame() {
        def boardGame = new BoardGame(
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

        def boardGameResult =  BoardGame.findByObjectId('123456')
        assert boardGameResult
        assert boardGameResult.name.equals('MyFakeGame')
    }
}
