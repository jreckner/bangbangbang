package com.wireblend

import grails.converters.JSON

class BoardGameController {

    def boardGameService
    def userService

    def scaffold = BoardGame

    static defaultAction = "list"

    def searchBoardGames = {
        HashMap jsonMap = new HashMap()
        jsonMap.put("sEcho", params.sEcho)

        def boardGames = []
        if(params.exact)
            boardGames = boardGameService.searchGamesByName(params.searchKeyword, true)
        else
            boardGames = boardGameService.searchGamesByName(params.searchKeyword, false)

        jsonMap.put("iTotalRecords", boardGames.size())
        jsonMap.put("iTotalDisplayRecords", boardGames.size())
        jsonMap.put("boardGames", boardGames)

        render jsonMap as JSON
    }

    def getBoardGameDetails = {
        def boardGameInstance = boardGameService.getGameDetails(params.objectId)
        if (!boardGameInstance) {
            return [] as JSON
        }
        render boardGameInstance as JSON
    }

    def assignBoardGameToUser = {
        def boardGame = boardGameService.getGameDetails(params.objectId)
        boardGameService.addToUserCollection(params.username, boardGame)

        // Should we return 201 and a link to this users games?
        render new HashMap() as JSON
    }

    def removeBoardGameFromUser = {
        def boardGame = boardGameService.getGameDetails(params.objectId)
        boardGameService.removeFromUserCollection(params.username, boardGame)

        // Should we return 201 and a link to this users games?
        render new HashMap() as JSON
    }

    def getAllBoardGames = {
        def boardGames = boardGameService.findAll()

        HashMap jsonMap = new HashMap()
        jsonMap.put("sEcho", params.sEcho)

        jsonMap.put("iTotalRecords", boardGames.size())
        jsonMap.put("iTotalDisplayRecords", boardGames.size())
        jsonMap.put("boardGames", boardGames)

        render jsonMap as JSON
    }

    def getAllBoardGamesForUser = {
        def boardGames = userService.getUser(params.username).boardGames

        HashMap jsonMap = new HashMap()
        jsonMap.put("sEcho", params.sEcho)

        jsonMap.put("iTotalRecords", boardGames.size())
        jsonMap.put("iTotalDisplayRecords", boardGames.size())
        jsonMap.put("boardGames", boardGames)

        render jsonMap as JSON
    }
}
