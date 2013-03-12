package com.wireblend

/**
 * Created with IntelliJ IDEA.
 * User: jreckner
 * Date: 3/11/13
 * Time: 11:09 PM
 * Simple Password validator
 * Criteria:
 *  Password is at least 5 characters.
 */
class PasswordValidator {
    public static Boolean validatePasswordCriteria(password) {
        if (password.size() > 5)
            return true
        false
    }
}
