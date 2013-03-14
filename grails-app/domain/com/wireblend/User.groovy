package com.wireblend

class User {
    String username
    String passwordHash
    Boolean locked = true
    //Date loginPeriodStartTimestamp
    //Date lastLoginAttemptTimestamp
    Integer loginAttemptCount = 0
    String activationKey
    
    static hasMany = [ roles: Role, permissions: String, boardGames: BoardGame ]

    static constraints = {
        username(nullable: false, blank: false, unique: true, email: true)
    }

    String toString() {
        "$username"
    }

    static mapping = {
        table 'account'
    }
}
