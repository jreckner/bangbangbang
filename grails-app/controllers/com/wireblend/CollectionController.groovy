package com.wireblend

import grails.converters.JSON

class CollectionController {

    def boardGameService
    def userService

    def index() {}

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
