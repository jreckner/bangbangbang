package com.wireblend

class RegistrationService {

    def userService
    def mailService

    def register(username, password, baseUrl) {
        def user = userService.createLockedUser(username, password)
        if (user) {
            // TODO replace this link with gsp template for pretty email
            def link = '<a href="' + baseUrl + 'registration/activate/' + user.userActivation.activationKey+ '">Click Here to activate your Board Games Central account.</a>'
            sendActivationEmail(username, user.userActivation.activationKey, link)
            return true
        }
        false
    }

    def activate(activationKey) {
        def userActivation = UserActivation.findByActivationKey(activationKey)
        if (userActivation)
        {
            def user = User.findByUserActivation(userActivation)
            if (user) {
                user.locked = false
                user.userActivation = null
                user.save()

                // Clear the activationKey from the db
                userActivation.delete()
                return true
            }
        }
        return false
    }

    private void sendActivationEmail (username, activationKey, link) {
        mailService.sendMail {
            to username
            subject "Board Games Central Activation Notification"
            html link
        }
    }
}
