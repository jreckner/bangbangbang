package com.wireblend

import java.net.URLEncoder

class BoardGameGeekXmlApiService {

    def boardGameGeekBaseUrl = 'http://www.boardgamegeek.com/xmlapi'
    def boardGameGeekSearchUrl = '/search?search='
    def boardGameGeekSearchExactSearchOption = '&exact=1'
    def boardGameGeekGameDetailsUrl = '/boardgame/'

    def searchBoardGameGeekExact(searchString) {
        def boardgames = new XmlSlurper().parse(boardGameGeekBaseUrl+boardGameGeekSearchUrl+URLEncoder.encode(searchString)+boardGameGeekSearchExactSearchOption)
        boardgames.boardgame.each() { p ->
            log.info(p.@objectid)
            log.info(p.name.text())
        }
    }

    def getBoardGameGeekDetailsById(id) {
        def boardGames = new XmlSlurper().parse(boardGameGeekBaseUrl+boardGameGeekGameDetailsUrl+id)
        def p = boardGames.boardgame[0]
        log.info(p.@objectid)
        log.info(p.name[0].text())
        log.info(p.age.text())
        log.info(p.minplayers.text())
        log.info(p.maxplayers.text())
        log.info(p.description.text())
        log.info(p.yearpublished.text())
        log.info(p.image.text())
        log.info(p.thumbnail.text())
        log.info(p.playingtime.text())

        def boardGame = new BoardGame(
                objectId: p.@objectid,
                name: p.name[0].text(),
                age: Integer.parseInt(p.age.text()),
                minPlayers: Integer.parseInt(p.minplayers.text()),
                maxPlayers: Integer.parseInt(p.maxplayers.text()),
                description: p.description.text(),
                yearPublished: Integer.parseInt(p.yearpublished.text()),
                image: p.image.text(),
                thumbnail: p.thumbnail.text(),
                playingTime: Integer.parseInt(p.playingtime.text())
        )
        boardGame.save()
        return boardGame // return the first one we find with this objectId
    }
}
