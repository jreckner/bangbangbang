package com.wireblend

import grails.converters.JSON

class BoardGameController {

    def scaffold = BoardGame
    static defaultAction = "list"

    def boardGameService
    def userService

    def index() {}

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
        boardGame = boardGameService.createBoardGame(boardGame) // Transfer this DTO to the DB
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
        if(params.players) {
            def filteredBoardGames = []
            boardGames.each() {
                if((Integer.parseInt(params.players) >= it.minPlayers) && (Integer.parseInt(params.players) <= it.maxPlayers))
                    filteredBoardGames.add(it)
            }
            boardGames = filteredBoardGames
        }
        if(params.age) {
            def filteredBoardGames = []
            boardGames.each() {
                if(Integer.parseInt(params.age) >= it.age)
                    filteredBoardGames.add(it)
            }
            boardGames = filteredBoardGames
        }
        if(params.playingTime) {
            def filteredBoardGames = []
            boardGames.each() {
                if(Integer.parseInt(params.playingTime) >= it.playingTime)
                    filteredBoardGames.add(it)
            }
            boardGames = filteredBoardGames
        }
        boardGames = boardGames.sort { it.name }

        HashMap jsonMap = new HashMap()
        jsonMap.put("sEcho", params.sEcho)

        jsonMap.put("iTotalRecords", boardGames.size())
        jsonMap.put("iTotalDisplayRecords", boardGames.size())
        jsonMap.put("boardGames", boardGames)

        render jsonMap as JSON
    }
}
