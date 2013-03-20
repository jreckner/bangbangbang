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

        def boardGame = new BoardGame(
                objectId: p.@objectid.text(),
                name: primaryName,
                age: Integer.parseInt(p.age.text()),
                minPlayers: Integer.parseInt(p.minplayers.text()),
                maxPlayers: Integer.parseInt(p.maxplayers.text()),
                description: p.description.text(),
                yearPublished: Integer.parseInt(p.yearpublished.text()),
                image: p.image.text(),
                thumbnail: p.thumbnail.text(),
                playingTime: Integer.parseInt(p.playingtime.text())
        )
        boardGame.validate()
        boardGame.save(failOnError: true, flush: true)
        return boardGame
    }
}
