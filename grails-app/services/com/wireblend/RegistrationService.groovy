package com.wireblend

class RegistrationService {

    def getNewActivationKey() {
        return UUID.randomUUID() as String
    }

    def activate(activationKey) {
        def user = User.findByActivationKey(activationKey)
        if (user) {
            user.locked = false
            user.save()
            return true
        }
        return false
    }

    def sendActivationEmail (username, activationKey, link) {
        sendMail {
            to username
            subject "Board Games Central Activation Notification"
            html link
        }
    }
}