package com.wireblend

import java.net.URLEncoder

class BoardGameGeekXmlApiService {

    def boardGameGeekBaseUrl = 'http://www.boardgamegeek.com/xmlapi'
    def boardGameGeekSearchUrl = '/search?search='
    def boardGameGeekSearchExactSearchOption = '&exact=1'
    def boardGameGeekGameDetailsUrl = '/boardgame/'

    def searchBoardGameGeek(searchString) {
        def boardgames = new XmlSlurper().parse(boardGameGeekBaseUrl+boardGameGeekSearchUrl+URLEncoder.encode(searchString))
        def boardGameSearchResults = []
        boardgames.boardgame.each() { p ->
            log.info(p.@objectid)
            log.info(p.name.text())
            boardGameSearchResults.add(new BoardGameDTO(name: p.name.text(), objectId: p.@objectid))
        }
        return boardGameSearchResults;
    }

    def searchBoardGameGeekExact(searchString) {
        def boardgames = new XmlSlurper().parse(boardGameGeekBaseUrl+boardGameGeekSearchUrl+URLEncoder.encode(searchString)+boardGameGeekSearchExactSearchOption)
        def boardGameSearchResults = []
        boardgames.boardgame.each() { p ->
            log.info(p.@objectid)
            log.info(p.name.text())
            boardGameSearchResults.add(new BoardGameDTO(name: p.name.text(), objectId: p.@objectid))
        }
        return boardGameSearchResults;
    }

    def getBoardGameGeekDetailsById(id) {
        def boardGames = new XmlSlurper().parse(boardGameGeekBaseUrl+boardGameGeekGameDetailsUrl+id)
        def p = boardGames.boardgame[0]  // Take the first result it SHOULD be exact because the id should be unique.
        log.info(p.@objectid.text())
        log.info(p.name[0].text())
        log.info(p.age.text())
        log.info(p.minplayers.text())
        log.info(p.maxplayers.text())
        log.info(p.description.text())
        log.info(p.yearpublished.text())
        log.info(p.image.text())
        log.info(p.thumbnail.text())
        log.info(p.playingtime.text())

        def primaryName = 'unknown'
        if(p.name.size() > 1) {
            p.name.each { n ->
                if(Boolean.parseBoolean(n.@primary.text())) {
                    primaryName = n.text()
                }
            }
        }
        else {
            primaryName = p.name[0].text()
        }

        def boardGame = new BoardGameDTO()
        boardGame.objectId = p.@objectid.text()
        boardGame.name = primaryName
        boardGame.age = Integer.parseInt(p.age.text())
        boardGame.minPlayers = Integer.parseInt(p.minplayers.text())
        boardGame.maxPlayers = Integer.parseInt(p.maxplayers.text())
        boardGame.description = p.description.text()
        boardGame.yearPublished = Integer.parseInt(p.yearpublished.text())
        boardGame.image = p.image.text()
        boardGame.thumbnail = p.thumbnail.text()
        boardGame.playingTime = Integer.parseInt(p.playingtime.text())
        return boardGame
    }
}
