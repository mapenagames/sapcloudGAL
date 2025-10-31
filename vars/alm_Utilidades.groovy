import groovy.util.XmlSlurper
import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput
import java.security.MessageDigest
import java.text.SimpleDateFormat

import hudson.model.Result
import hudson.model.Run

import groovy.json.JsonSlurper

def currentDate() {
    def now = new Date()
    env.tdate = now.format("dd/MM/yyyy HH:mm:ss", TimeZone.getTimeZone('America/Buenos_Aires'))
    env.tdate2 = now.format("dd/MM/yyyy-HH:mm:ss", TimeZone.getTimeZone('America/Buenos_Aires'))
    env.tdate3 = now.format("ddMMyyyyHHmmss", TimeZone.getTimeZone('America/Buenos_Aires'))
    env.tdate4 = now.format("dd/MM/yyyy-HH:mm", TimeZone.getTimeZone('America/Buenos_Aires'))
    env.tdatealm = now.format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('America/Buenos_Aires'))
    env.year = now.format("yyyy", TimeZone.getTimeZone('America/Buenos_Aires'))
    env.tdateDMY = now.format("dd/MM/yyyy", TimeZone.getTimeZone('America/Buenos_Aires'))
}

def messages(String msg, String type, String obj = "") {
    switch( type ) {
        case "title":
            def text="${msg}".toUpperCase()
            print "\n◼️◼️◼️ ${text} ◼️◼️◼️\n"
            break
        case "info":
            print "📝INFO »» ${msg}"
            break
        case "warn":
            print "👀ATENCIÓN »» ${msg}"
            break
        case "success":
            print "\n✔️OK »» ${msg}"
            break
        case "error":
            print "🔥ERROR »» Oops! Hubo un error durante la ejecución: ${msg}"
            break
        case "alm":
            print "${msg}"
            break
        case "inputs":
            print "🔹Opciones seleccionadas:\n${msg}"
            break
        case "debug":
            if( env.DEBUG ) {
                println "🐛 [DEBUG] ${msg}"
                print "${obj}"
            }
            break
    }
}

def soyAlm(){
    String currentUserId = currentBuild.getBuildCauses()[0].userId

    if( (currentUserId == "l0170909") || (currentUserId == "l0699641") || (currentUserId == "l0284726") || (currentUserId == "l1011097")) {
        println "Soy del Equipo ALM"
        env.DEBUG = "true"
        env.USER_ALM = "true"
        return true 
    }
}

def string2ListMap(String rawDeploy) {
    def jsonLike = rawDeploy
        .replaceAll(/([a-zA-Z0-9_]+)=/, '"$1":') 
        .replaceAll("\\\\", "\\\\\\\\")      
        .replaceAll(/"(.*?)":\s*([^",}\]]+)/) { 
            def key = it[1]
            def value = it[2]
            return "\"${key}\": \"${value}\""
        }

    //deployLista.each { item ->
    //    println "Servidor: ${item.server}, Tipo: ${item.deploy_tipo}, Path: ${item.pathInstall}, SO: ${item.so}"
    //}
return new JsonSlurper().parseText(jsonLike)
}


def jsonParse(String json) {
    def resultJson = new JsonSlurperClassic().parseText(json)
    return resultJson
}


def jsonStr(def obj, boolean pretty = false) {
    String result

    if( obj.getClass() == String && pretty ) {
        result = JsonOutput.prettyPrint( obj )
    }
    else {
        result = JsonOutput.toJson( obj )
        if( pretty ) {
            result = JsonOutput.prettyPrint( result )
        }
    }
    return result
}
def getResumenRM(def props){
    def resumenRM = input(
        id: 'resumenRM',
        message: "Resumen de Implementación: " + "\n" + "Origen: ${env.DTSX_NOMBRE_REPO}  " + "\n" + "Destino: ${env.DEPLOY_ENV} " + "\n" + "Servidor/Consola: ${env.RM_SERVER_DTSX}",
        ok: 'Continuar',
    )
}
def logRotator(){
    /*
    logRotator()
    inputs:
        -> env.JENKINS_LOG_ROTATOR  // define la cantidad de corridas que se mantiene en jenkins
    */
    properties([
            buildDiscarder(logRotator(numToKeepStr: env.JENKINS_LOG_ROTATOR)),
            ])
}
def disableActualBuilds( boolean abortActualBuild = false){
    /*
    DisableActualBuilds()
    inputs:
        -> abortActualBuild  ==> 
                                true : para abortar la que está en ejecucion y ejecuta la nueva corrida.
                                        Se ejecute pipeline #1
                                        Se ejecute pipeline #2  (mata al #1)
                                false: para poner en cola la nueva ejecucion
                                        Se ejecute pipeline #1
                                        Se ejecuta pipeline #2 (queda en espera hasta que termine el #1)
    outputs:
    *************************************************************
    */
    if ( abortActualBuild ) {
        properties([
            disableConcurrentBuilds(abortPrevious: true),
            ])
    }else{
            properties([
            disableConcurrentBuilds(),
            ])  
    }
} 

def existBuildsEnVuelo() {

    /*
    veoBuildsEnVuelo()
        valida si existe otra corrida anterior a la actual
        REQUIERE:
        import hudson.model.Result
        import hudson.model.Run
    
    inputs:
    outputs: 
        true : existes corridas anteriores en vuelo anterior
        false: No existe corridas anteriores en vuelo anterior

    */

    Run previousBuild = currentBuild.rawBuild.getPreviousBuildInProgress()
    while (previousBuild != null) {
        if (previousBuild.isInProgress()) {
            def executor = previousBuild.getExecutor()
            println(previousBuild)
            if (executor != null) {
                return true
            }
        }

        previousBuild = previousBuild.getPreviousBuildInProgress()
    }
    return false
}

def removerEmojis(String input) {
    if (input == null) return ""
    
    // Elimina surrogate pairs (emojis fuera del BMP)
    def sinSurrogates = input.replaceAll("[\\uD800-\\uDBFF][\\uDC00-\\uDFFF]", "")
    
    // Elimina emojis simples y caracteres de presentación (símbolos y otros)
    def sinOtrosEmojis = sinSurrogates.replaceAll("[\\p{So}\\p{Cn}\\u200D]", "") // \u200D = ZWJ

    return sinOtrosEmojis.trim()
}

def seleccionarFile(String nombreDestinoPorDefecto = 'archivo_subido.txt') {
    /*
    obj: Seleccionar un archivo de un folder de la pc
    */
    if ( env.USER_ALM ) {
        println('***********************************************************************************')
        println('***** seleccionarFile')
        println('***********************************************************************************')
    }

    def resp = input message: 'Seleccioná un archivo',
                     parameters: [
                       base64File('file'),                                   // contenido en Base64
                       string(name: 'filename',
                              defaultValue: nombreDestinoPorDefecto,
                              description: 'Nombre con el que guardarlo/mostrarlo')
                     ]
    // 2) Extraer valores del input
    def base64 = resp.file
    def nombreArchivo = (resp.filename ?: nombreDestinoPorDefecto).trim()
    // Saneamos el nombre (evitar separadores de ruta)
    nombreArchivo = nombreArchivo.replaceAll(/[\\\\\\/]+/, '_')

    // 3) Decodificar y guardar en el workspace con ese nombre
    writeFile file: nombreArchivo, encoding: 'Base64', text: base64

}