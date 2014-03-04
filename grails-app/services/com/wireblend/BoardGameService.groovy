package com.wireblend

class BoardGameService {

    def boardGameGeekXmlApiService
    def userService

    def findAll() {
        return BoardGame.findAll()
    }

    def searchGamesByName(searchKeyword) {
        return searchGamesByName(searchKeyword, false)
    }

    def createBoardGame(boardGameDTO) {
        def boardGame = BoardGame.findByObjectId(boardGameDTO.objectId)
        if (!boardGame) {
            boardGame = new BoardGame(
                    objectId: boardGameDTO.objectId,
                    name: boardGameDTO.name,
                    age: boardGameDTO.age,
                    minPlayers: boardGameDTO.minPlayers,
                    maxPlayers: boardGameDTO.maxPlayers,
                    description: boardGameDTO.description,
                    yearPublished: boardGameDTO.yearPublished,
                    image: boardGameDTO.image,
                    thumbnail: boardGameDTO.thumbnail,
                    playingTime: boardGameDTO.playingTime
            )
            boardGame.validate()
            boardGame.save()
        }
        return boardGame
    }

    def searchGamesByName(searchKeyword, exact) {
        def boardGames = []
        def boardGameDTOList
        if (exact)
            boardGameDTOList =  boardGameGeekXmlApiService.searchBoardGameGeekExact(searchKeyword)
        else
            boardGameDTOList = boardGameGeekXmlApiService.searchBoardGameGeek(searchKeyword)

        boardGameDTOList.each() { dto ->
            boardGames.add(getGameDetails(dto.objectId))
        }
        return boardGames
    }

    def getGameDetails(objectId) {
        // TODO see if we already have the game stored in our db
        def boardGame = BoardGame.findByObjectId(objectId)
        if (boardGame)
            return boardGame
        return boardGameGeekXmlApiService.getBoardGameGeekDetailsById(objectId)
    }

    def addToUserCollection(username, boardGame) {
        User user = null
        if (username instanceof User)
            user = username
        else
            user = userService.getUser(username)

        // if we found a user, add our game!
        if (user)
        {
            user.addToBoardGames(boardGame as BoardGame)
            user.save(flush: true)
        }

    }

    def removeFromUserCollection(username, boardGame) {
        User user = null
        if (username instanceof User)
            user = username
        else
            user = userService.getUser(username)

        // if we found a user, add our game!
        if (user)
        {
            user.removeFromBoardGames(boardGame as BoardGame)
            user.save(flush: true)
        }

    }
}
