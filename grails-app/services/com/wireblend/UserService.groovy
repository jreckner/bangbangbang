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

            allowUserAccountAccess(username)
        }
        return user
    }

    def allowUserAccountAccess(username) {
        // Add Permissions to show user
        def user = User.findByUsername(username)
        user.addToPermissions("user:show,edit:"+user.id)
        user.save()
    }

    def getUser(username) {
        User.findByUsername(username);
    }
}
