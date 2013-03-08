import org.apache.shiro.crypto.hash.Sha256Hash
import com.wireblend.User
import com.wireblend.Role

class BootStrap {

    def init = { servletContext ->

        // Create the admin role
        def adminRole = Role.findByName('ROLE_ADMIN') ?:
            new Role(name: 'ROLE_ADMIN').save(flush: true, failOnError: true)

        // Create the user role
        def userRole = Role.findByName('ROLE_USER') ?:
            new Role(name: 'ROLE_USER').save(flush: true, failOnError: true)

        // Create an admin user
        def adminUser = new User(username: "bang@noreply.com",
                                passwordHash: new Sha256Hash("bang").toHex(),
                                locked: false,
                                activationKey: new String())
        adminUser.addToPermissions("*:*")
        adminUser.save()

        // Add roles to the admin user
        adminUser.addToRoles(adminRole)
                .addToRoles(userRole)
                .save(flush: true, failOnError: true)

    }
    def destroy = {
    }
}
