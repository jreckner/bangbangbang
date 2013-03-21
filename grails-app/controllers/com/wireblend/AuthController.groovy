package com.wireblend

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.web.util.SavedRequest
import org.apache.shiro.web.util.WebUtils
import org.apache.shiro.grails.ConfigUtils
import groovy.time.TimeCategory

class AuthController {
    def shiroSecurityManager

    def index = { redirect(action: "login", params: params) }

    def login = {
        flash.message = params.message
        return [ username: params.username, rememberMe: (params.rememberMe != null), targetUri: params.targetUri, message: params.message ]
    }

    def signIn = {
        def authToken = new UsernamePasswordToken(params.username, params.password as String)

        // Support for "remember me"
        if (params.rememberMe) {
            authToken.rememberMe = true
        }

        // If a controller redirected to this page, redirect back
        // to it. Otherwise redirect to the root URI.
        def targetUri = params.targetUri ?: "/"

        // Handle requests saved by Shiro filters.
        def savedRequest = WebUtils.getSavedRequest(request)
        if (savedRequest) {
            targetUri = savedRequest.requestURI - request.contextPath
            if (savedRequest.queryString) targetUri = targetUri + '?' + savedRequest.queryString
        }

        //Find out if this userId is locked before allowing any login.
        def user = User.findByUsername(params.username)
        if (user && user.locked) {
            // Keep the username and "remember me" setting so that the
            // user doesn't have to enter them again.
            def m = [ username: params.username, message: message(code: "login.locked") ]
            if (params.rememberMe) {
                m["rememberMe"] = true
            }

            // Remember the target URI too.
            if (params.targetUri) {
                m["targetUri"] = params.targetUri
            }

            // Now redirect back to the login page.
            redirect(action: "login", params: m)
        }
        else
        {
            try{
                // Perform the actual login. An AuthenticationException
                // will be thrown if the username is unrecognised or the
                // password is incorrect.
                SecurityUtils.subject.login(authToken)

                if(user.loginAttemptCount > 0) {
                    user.loginPeriodStartTimestamp = null
                    // user.lastLoginAttemptTimestamp = null
                    user.loginAttemptCount = 0
                    user.save()
                }

                log.info "Redirecting to '${targetUri}'."
                redirect(uri: targetUri)
            }
            catch (AuthenticationException ex){
                // Keep the username and "remember me" setting so that the
                // user doesn't have to enter them again.
                // Authentication failed, so display the appropriate message
                def m = [ username: params.username, message: message(code: "login.failed") ]
                log.info "Authentication failure for user ${params.username}"

                if (params.rememberMe) {
                    m["rememberMe"] = true
                }

                // Remember the target URI too.
                if (params.targetUri) {
                    m["targetUri"] = params.targetUri
                }

                if(user) {
                    // First Failure sets our timestamps to keep track of retries
                    if(user.loginPeriodStartTimestamp == null) {
                        user.loginPeriodStartTimestamp = new Date()
                        user.lastLoginAttemptTimestamp = new Date()
                    }
                    else {
                        // Subsequent failures
                        user.lastLoginAttemptTimestamp = new Date()
                    }
                    user.incrementLoginAttemptCount()
                    user.save()

                    // request.getRemoteAddr()
                    // request.getHeader("X-Forwarded-For")
                    // request.getHeader("Client-IP")
                    println("${user.username} has login attempts: ${user.getLoginAttemptCount()} from ${request.getRemoteAddr()}")
                    log.warn("${user.username} has login attempts: ${user.getLoginAttemptCount()} from ${request.getRemoteAddr()}")

                    if(user.loginAttemptCount > 5) {
                        def duration = TimeCategory.minus(user.lastLoginAttemptTimestamp, user.loginPeriodStartTimestamp)
                        if(duration.getMinutes() < 5) {
                            redirect(action: "accessdenied")
                        }
                        else {
                            user.loginPeriodStartTimestamp = new Date()
                            // user.lastLoginAttemptTimestamp = new Date()
                            user.resetLoginAttemptCount()
                            user.save()
                            redirect(action: "login", params: m)
                        }
                    }
                    else {
                        // Now redirect back to the login page.
                        redirect(action: "login", params: m)
                    }
                }
                else {
                    // Now redirect back to the login page.
                    redirect(action: "login", params: m)
                }
            }
        }
    }

    def signOut = {
        // Log the user out of the application.
        def principal = SecurityUtils.subject?.principal
        SecurityUtils.subject?.logout()
        // For now, redirect back to the home page.
        if (ConfigUtils.getCasEnable() && ConfigUtils.isFromCas(principal)) {
            redirect(uri:ConfigUtils.getLogoutUrl())
        }else {
            redirect(uri: "/")
        }
        ConfigUtils.removePrincipal(principal)
    }

    def unauthorized = {
        render "You do not have permission to access this page."
    }
}
