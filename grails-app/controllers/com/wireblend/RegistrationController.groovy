package com.wireblend

import org.apache.shiro.SecurityUtils

class RegistrationController {

    def registrationService

    def userService

    static defaultAction = "index"

    def index() {

        def user
        if(SecurityUtils.subject.isAuthenticated()) {
            user = userService.getUser(SecurityUtils.subject.getPrincipal())
            [user: user]
            redirect(controller: "user", action: "show", id: user.id)
        }
        else {
            user = new User()
            [user: user]
        }

        return [ username: params.username, message: params.message ]
    }

    def activate = {
        if(registrationService.activate(params.activationKey))
        {
            //flash.message = message(code: "useraccount is activated and unlocked.")
            log.debug("useraccount is activated and unlocked.")
            // redirect(uri: "/registration/activationComplete")

            // Keep the username so that the
            // user doesn't have to enter it again.
            def m = [ username: params.username, message: "useraccount is activated and unlocked." ]
            // Now redirect back to the login page.
            redirect(controller: "auth", action: "login", params: m)
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

    def register() {

        // Check to see if the username already exists
        def user = userService.getUser(params.username)
        if (user) {
            flash.message = "User already exists with the username '${params.username}'"
            redirect(action: "index")
        }
        else {
            // User doesn't exist with username. Let's create one

            // Make sure the passwords match
            if (params.password != params.password2) {
                flash.message = "Passwords do not match"
                def m = [username: params.username]
                redirect(action: "index", params: m)
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
                    flash.message = "Unexpected Registration Failure, please try again"
                    redirect(action: "index")
                }
            }
        }
    }
}
