package pl.emil.users.security

enum class AuthenticationErrorEnum {
    INCORRECT_SERVICE_CREDENTIALS,
    GENERIC_ERROR,
    TOKEN_JWT_MANCANTE,
    TOKEN_JWT_SCADUTO,
    TOKEN_JWT_NON_CORRETTO,
    TOKEN_JWT_NESSUN_UTENTE_TROVATO
}
