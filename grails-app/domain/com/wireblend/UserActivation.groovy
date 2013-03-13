package com.wireblend

class UserActivation {

    String activationKey = UUID.randomUUID() as String
    Date activationStartDate = new Date()

    static belongsTo = User

    static constraints = {
        activationKey(nullable: false, blank: false, unique: true)
        activationStartDate(nullable: true)
    }
}
