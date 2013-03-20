package com.wireblend

class BoardGame {

    String objectId
    String name
    Integer yearPublished = 0
    Integer minPlayers = 0
    Integer maxPlayers = 0
    Integer playingTime = 0
    Integer age = 0
    String description
    String thumbnail
    String image

    static hasMany = [ users: User ]
    static belongsTo = User

    static constraints = {
        objectId(nullable: false, unique: true)
    }

    static mapping = {
        description type: "text"
    }

    String toString() {
        "$name - $yearPublished"
    }
}
