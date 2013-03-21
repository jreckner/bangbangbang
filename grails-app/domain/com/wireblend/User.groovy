package com.wireblend

class User {
    String username
    String passwordHash
    Boolean locked = true
    Date loginPeriodStartTimestamp
    Date lastLoginAttemptTimestamp
    UserActivation userActivation

    Integer loginAttemptCount = 0

    static hasMany = [ roles: Role, permissions: String, boardGames: BoardGame ]

    static constraints = {
        username(nullable: false, blank: false, unique: true, email: true)
        userActivation(unique: true, nullable: true)
        loginPeriodStartTimestamp(nullable: true)
        lastLoginAttemptTimestamp(nullable: true)
    }

    // save() must be invoked by the caller!
    def void incrementLoginAttemptCount() {
        loginAttemptCount++
    }

    def void resetLoginAttemptCount() {
        loginAttemptCount = 0
    }

    String toString() {
        "$username"
    }

    static mapping = {
        table 'account'
    }
}
