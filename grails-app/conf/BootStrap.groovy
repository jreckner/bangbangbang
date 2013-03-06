import org.apache.shiro.crypto.hash.Sha256Hash
import com.wireblend.User

class BootStrap {

    def init = { servletContext ->
        def user = new User(username: "bang", passwordHash: new Sha256Hash("bang").toHex())
        user.addToPermissions("*:*")
        user.save()
    }
    def destroy = {
    }
}
