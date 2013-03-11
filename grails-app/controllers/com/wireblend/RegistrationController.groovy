package com.wireblend

class RegistrationController {

    def registrationService

    def userService

    static defaultAction = "index"

    def index() {
        User user = new User()
        [user: user]
    }

    def activate = {
        if(registrationService.activate(params.activationKey))
        {
            //flash.message = message(code: "useraccount is activated and unlocked.")
            log.debug("useraccount is activated and unlocked.")
            //TODO: redirect to an activation succeeded page
        }
        else
        {
            //flash.message = message(code: "unable to find useraccount for activation.")
            log.debug("unable to find useraccount for activation.")
            //TODO: redirect to an activation failed page
        }
        // Now redirect back to the login page.
        redirect(uri: "/")
    }

    def register() {

        // Check to see if the username already exists
        def user = userService.getUser(params.username)
        if (user) {
            flash.message = "User already exists with the username '${params.username}'"
            render('index')
        }

        // User doesn't exist with username. Let's create one
        else {

            // Make sure the passwords match
            if (params.password != params.password2) {
                flash.message = "Passwords do not match"
                render('index')
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

                    redirect(uri: "/")
                }
                else {
                    //TODO: redirect to an registration failed page
                }
            }
        }
    }
}
