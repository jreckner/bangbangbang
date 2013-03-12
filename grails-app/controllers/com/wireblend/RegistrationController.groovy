package com.wireblend

class RegistrationController {

    def registrationService

    def userService

    static defaultAction = "index"

    def index() {
        User user = new User()
        [user: user]
    }

    def activationComplete() {

    }

    def activate = {
        if(registrationService.activate(params.activationKey))
        {
            //flash.message = message(code: "useraccount is activated and unlocked.")
            log.debug("useraccount is activated and unlocked.")
            // redirect(uri: "/registration/activationComplete")

            // Keep the username and "remember me" setting so that the
            // user doesn't have to enter them again.
            def m = [ username: params.username ]
            // Now redirect back to the login page.
            // redirect(action: "activationComplete", params: m)
            redirect(action: "activationComplete")
        }
        else
        {
            log.debug("unable to find useraccount for activation.")
            flash.message = message(code: "unable to find useraccount for activation.")
            redirect(action: "registrationFailure")
        }
    }

    def registrationComplete() {
    }

    def registrationFailure() {
    }

    def register() {

        // Check to see if the username already exists
        def user = userService.getUser(params.username)
        if (user) {
            flash.message = "User already exists with the username '${params.username}'"
            redirect(action: "registrationFailure")
        }

        // User doesn't exist with username. Let's create one
        else {

            // Make sure the passwords match
            if (params.password != params.password2) {
                flash.message = "Passwords do not match"
                redirect(action: "registrationComplete")
            }

            // Passwords match. Let's attempt to save the user
            else {
                // Create user
                def activationKey = registrationService.getNewActivationKey()
                if (userService.createLockedUser(params.username, params.password, activationKey)) {

                    // TODO replace this link with gsp template for pretty email
                    def link = '<a href="' + createLink(uri: '/', absolute: true) + 'registration/activate/' + activationKey + '">Click Here to activate your Board Games Central account.</a>'
                    registrationService.sendActivationEmail(params.username, activationKey, link)

                    // Login user
                    //def authToken = new UsernamePasswordToken(user.username, params.password)
                    //SecurityUtils.subject.login(authToken)

                    //redirect(uri: "/")
                    redirect(action: "registrationComplete")
                }
                else {
                    //TODO: redirect to an registration failed page

                    // Now redirect back to the login page.
                    redirect(uri: "registrationFailure")
                }
            }
        }
    }
}
