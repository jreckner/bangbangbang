import com.wireblend.UserActivation
import org.apache.shiro.crypto.hash.Sha256Hash
import com.wireblend.User
import com.wireblend.Role

class BootStrap {

    def init = { servletContext ->

        // Create the admin role
        def adminRole = Role.findByName('ROLE_ADMIN') ?:
            new Role(name: 'ROLE_ADMIN')
        adminRole.addToPermissions("*:*")
        adminRole.save(flush: true, failOnError: true)

        // Create the user role
        def userRole = Role.findByName('ROLE_USER') ?:
            new Role(name: 'ROLE_USER')
        userRole.addToPermissions("about:*")
        userRole.addToPermissions("auth:*")
        userRole.addToPermissions("boardGame:*")
        userRole.addToPermissions("registration:*")
        userRole.save(flush: true, failOnError: true)


        // Create an admin user
        def adminUser = new User(username: "bang@noreply.com",
                                passwordHash: new Sha256Hash("bang").toHex(),
                                locked: false)
        adminUser.addToPermissions("*:*")
        adminUser.save()

        // Add roles to the admin user
        adminUser.addToRoles(adminRole)
                .addToRoles(userRole)
                .save(flush: true, failOnError: true)

        // Create jon.reckner user
        def jonUser = new User(username: "jon.reckner@gmail.com",
                passwordHash: new Sha256Hash("R@nger90").toHex(),
                locked: false)
        jonUser.addToRoles(Role.findByName('ROLE_USER'))
        jonUser.save(flush: true, failOnError: true)

        // Add Permissions to show user
        def user = User.findByUsername(jonUser.username)
        user.addToPermissions("user:show,edit:"+user.id)
        user.save(flush: true, failOnError: true)

    }
    def destroy = {
    }
}
