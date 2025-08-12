import StCo.UserPasswordEncoderListener
import org.springframework.security.crypto.password.PasswordEncoder

beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener) {
        passwordEncoder = ref('passwordEncoder')
    }
}
