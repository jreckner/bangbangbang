package com.wireblend

import org.apache.shiro.grails.ShiroSecurityService

class UserService {

    def shiroSecurityService
    def user

    def createLockedUser(username, password) {
        if (PasswordValidator.validatePasswordCriteria(password))
        {
            user = new User(
                    username: username,
                    passwordHash: shiroSecurityService.encodePassword(password),
                    locked: true,
                    userActivation: new UserActivation().save(flush: true))

            // Add USER role to new user
            user.addToRoles(Role.findByName('ROLE_USER'))
            user.save()
        }
        return user
    }

    def getUser(username) {
        User.findByUsername(username);
    }
}
