package com.wireblend

import org.apache.shiro.grails.ShiroSecurityService

class UserService {

    def shiroSecurityService
    def user

    def createLockedUser(username, String password, activationKey) {
        if (PasswordValidator.validatePasswordCriteria(password))
        {
            user = new User(
                    username: username,
                    passwordHash: shiroSecurityService.encodePassword(password),
                    locked: true,
                    activationKey: activationKey
            )
            user.save()

            // Add USER role to new user
            user.addToRoles(Role.findByName('ROLE_USER'))
        }
        return user
    }

    def getUser(username) {
        User.findByUsername(username);
    }
}
