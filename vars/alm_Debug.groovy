/*
    alm_Debug.printEnvVars()

    inputs:
        -> env.DEBUG
    outputs:
        <-
*/
def printEnvVars() {
    if( env.DEBUG ) {
        String text = "Variables de entorno:\n"
        env.getEnvironment().each { name, value -> text = text + "${name} = ${value}\n" }
        alm_Utilidades.messages(text, "debug")
    }
}

/*
    alm_Debug.printJSON(String text, def obj)

    inputs:
        -> text: Texto de referencia
        -> obj: hashMap o ArrayList o etc
        -> env.DEBUG
    outputs:
        <-
*/
def printJSON(String text, def obj) {
    if( env.DEBUG ) {
        String textObj = alm_Utilidades.jsonStr(obj, true)
        alm_Utilidades.messages(text, "debug", textObj)
    }
}