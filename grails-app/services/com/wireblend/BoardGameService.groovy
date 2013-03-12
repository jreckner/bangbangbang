package com.wireblend

class BoardGameService {

    def boardGameGeekXmlApiService
    def userService

    def searchGamesByName(searchKeyword) {
        return boardGameGeekXmlApiService.searchBoardGameGeek(searchKeyword)
    }

    def searchGamesByExactName(searchKeyword) {
        return boardGameGeekXmlApiService.searchBoardGameGeekExact(searchKeyword)
    }

    def getGameDetails(objectId) {
        // TODO see if we already have the game stored in our db
        return boardGameGeekXmlApiService.getBoardGameGeekDetailsById(objectId)
    }

    def addToUserCollection(username, boardGame) {
        User user = null
        if (username instanceof User)
            user = username
        else
            user = userService.getUser(user)

        // if we found a user, add our game!
        if (user)
            user.addToBoardGames(boardGame as BoardGame)
    }
}
