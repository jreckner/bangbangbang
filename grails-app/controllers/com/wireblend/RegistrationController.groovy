package com.wireblend

import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.SecurityUtils
import org.hibernate.connection.UserSuppliedConnectionProvider

class RegistrationController {
    def shiroSecurityService

    static defaultAction = "index"

    def index() {
        User user = new User()
        [user: user]
    }

    def activate = {
        def user = User.findByActivationKey(params.activationKey)
        if (user) {
            user.locked = false
            user.save()
            flash.message = message(code: "useraccount is activated and unlocked.")
        }
        else
        {
            flash.message = message(code: "unable to find useraccount for activation.")
            //TODO: redirect to an activation failed page
        }
        // Now redirect back to the login page.
        redirect(uri: "/")
    }

    def register() {

        // Check to see if the username already exists
        def user = User.findByUsername(params.username)
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
                def activationKey = UUID.randomUUID() as String
                user = new User(
                        username: params.username,
                        passwordHash: shiroSecurityService.encodePassword(params.password),
                        locked: true,
                        activationKey: activationKey
                )

                if (user.save()) {

                    // Add USER role to new user
                    user.addToRoles(Role.findByName('ROLE_USER'))

                    sendMail {
                        to params.username
                        subject "Board Games Central Activation Notification"
                        html '<a href="' + createLink(uri: '/', absolute: true) + 'registration/activate/' + activationKey + '">Click Here to activate your Board Games Central account.</a>'
                    }

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
