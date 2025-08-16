import StCo.UserPasswordEncoderListener

beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener) {
        passwordEncoder = ref('passwordEncoder')
    }
}
