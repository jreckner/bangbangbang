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

            // Keep the username so that the
            // user doesn't have to enter it again.
            // def m = [ username: params.username ]
            // Now redirect back to the login page.
            // redirect(action: "activationComplete", params: m)
            redirect(action: "activationComplete")
        }
        else
        {
            log.debug("unable to find useraccount for activation.")
            flash.message = "unable to find username '${params.username}' for activation."
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
                redirect(action: "registrationFailure")
            }

            // Passwords match. Let's attempt to save the user
            else {
                // Create user
                def baseUrl = createLink(uri: '/', absolute: true)
                if (registrationService.register(params.username, params.password, baseUrl))
                {
                    // Login user
                    //def authToken = new UsernamePasswordToken(user.username, params.password)
                    //SecurityUtils.subject.login(authToken)

                    //redirect(uri: "/")
                    redirect(action: "registrationComplete")
                }
                else {
                    // Now redirect back to the login page.
                    redirect(action: "registrationFailure")
                }
            }
        }
    }
}
