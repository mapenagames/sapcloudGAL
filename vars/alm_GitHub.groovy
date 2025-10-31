import java.util.Base64

def getRepos(){
    /*
    lista todos los repos de una env.GIT_ORGnizacion en github
    inputs:
        -> env.GIT_API_BASE
        -> env.GIT_ORG
    outputs:
        <- [repo: name_Repository , id: ID_repository , html_url: it.html_url]
    */        

    int pageSize = 100
    def listaRepos = []
    int pageNumber = 0

    boolean next = false
    while( !next ) {
        pageNumber++

        def response = httpRequest(
            authentication: env.GIT_CRED,
            quiet: (env.DEBUG == 'true' ? false : true),
            validResponseCodes: "100:499",
            customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github.mercy-preview+json', maskValue: false ]
            ],
            url:"${env.GIT_API_BASE}/orgs/${env.GIT_ORG}/repos?per_page=${pageSize}&page=${pageNumber}&sort=pushed")
        //println("Content: "+response.content)
        def responseJson = alm_Utilidades.jsonParse(response.content)
        if( responseJson.size() < pageSize ) {
            next = true
        }
        responseJson.each { listaRepos <<  [repo: it.name , id: it.id , html_url: it.html_url] }
    }
    return listaRepos
}

def cloneRepo() {
 
    String branchOrTag = env.GIT_BRANCH ? env.GIT_BRANCH : "refs/tags/${env.GIT_TAG}"
    
    if( env.GIT_NOMBRE_REPO ) {
        env.GIT_URL = "${env.GIT_BASE_URL}/${env.GIT_ORG}/${env.GIT_NOMBRE_REPO}.git"
    }
    println env.GIT_URL
    String gitAppTagUrl = env.GIT_URL.replace(".git", "/tree/${env.GIT_BRANCH ? env.GIT_BRANCH : env.GIT_TAG}")
    
    alm_Utilidades.messages("Descargando archivos de la Aplicacion","title")
    alm_Utilidades.messages("📦 Repositorio: ${gitAppTagUrl}", "info")
    alm_Utilidades.messages("🏷️ Branch o Tag: ${branchOrTag}", "info")

    checkout([
        $class: 'GitSCM',
        userRemoteConfigs: [
            [
                credentialsId: env.GIT_CRED,
                url: env.GIT_URL,
                name: "origin"
            ]
        ],
        branches: [[ name: branchOrTag ]],
        doGenerateSubmoduleConfigurations: false,
        extensions: [
            [
                $class: 'CloneOption',
                shallow: true,
                noTags: false,
                depth: 1
            ],
            [
                $class: 'SubmoduleOption',
                disableSubmodules: false,
                recursiveSubmodules: true,
                reference: '',
                trackingSubmodules: false
            ]
        ]
    ])
}

def getBranches() {
    /*
        Descripcion: Obtengo la lista de branch del app repo, con sus commit sha.
        Autor: 
        galGitHub.getBranches()
        inputs:
            -> env.GIT_ORG
            -> env.GIT_NOMBRE_REPO
            -> env.GIT_API_BASE
        outputs:
            <-  [ name: [ArrayList de branches] , commit: [ArrayList de commits] ]
    */
    int pageSize = 100

    def listaBranches = [ name: [], commit: []]
    int pageNumber = 0
    // No existe la estructura do..while en groovy
    boolean next = false
    while( !next ) {
        pageNumber++

        def response = httpRequest(
            authentication: env.GIT_CRED,
            quiet: (env.DEBUG == 'true' ? false : true),
            validResponseCodes: "100:599",
            customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github.mercy-preview+json', maskValue: false ]
            ],
            url:"${env.GIT_API_BASE}/repos/${env.GIT_ORG}/${env.GIT_NOMBRE_REPO}/branches?per_page=${pageSize}&page=${pageNumber}")
        // println("Content: "+response.content)

        def responseJson = alm_Utilidades.jsonParse(response.content)

        if( responseJson.size() < pageSize ) {
            next = true
        }

        listaBranches.name += responseJson*.name
        listaBranches.commit += responseJson*.commit.sha

        //galDebug.printJSON( "Return galGitHub.getBranches()" , listaBranches )  
    }
    return listaBranches
} 

def getTags() {
    int pageSize = 100

    def listaTags = [ name: [], commit: []]
    //def listaTags = []
    int pageNumber = 0
    // No existe la estructura do..while en groovy
    boolean next = false
    while( !next ) {
        pageNumber++

        def response = httpRequest(
            authentication: env.GIT_CRED,
            quiet: (env.DEBUG == 'true' ? false : true),
            validResponseCodes: "100:599",
            customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github.mercy-preview+json', maskValue: false ]
            ],
            url:"${env.GIT_API_BASE}/repos/${env.GIT_ORG}/${env.GIT_NOMBRE_REPO}/tags?per_page=${pageSize}&page=${pageNumber}")

        def responseJson = alm_Utilidades.jsonParse(response.content)

        if( responseJson.size() < pageSize ) {
            next = true
        }

        listaTags.name += responseJson*.name
        listaTags.commit += responseJson*.commit.sha

    }
    return listaTags
}

def getLastCommitFile() {
    /*
    Descripcion: .
    inputs:
        -> env.GIT_API_BASE
        -> env.TRACKING_JENKINS_FILE_COMMIT_REPO  (env.GIT_ORG / REPO)
        -> env.TRACKING_JENKINS_FILE_COMMIT       (FILE a buscar el ultimo commit)

    outputs: 
        <- 
        [
           responseJson[0].sha              //sha del commit
           responseJson[0].commit.message   //mensaje del commit
        ]
    */
    if ( env.USER_ALM ) {
        println('***********************************************************************************')
        println('***** def getLastCommitFile()  ******')
        println('***********************************************************************************')
    }

    def response = httpRequest(
            authentication: env.GIT_CRED,
            quiet: (env.DEBUG == 'true' ? false : true),
            httpMode:'GET',
            validResponseCodes: "100:599",
            customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github.mercy-preview+json', maskValue: false ]
            ],
            url:"${env.GIT_API_BASE}/repos/${env.TRACKING_JENKINS_FILE_COMMIT_REPO}/commits?path=${env.TRACKING_JENKINS_FILE_COMMIT}&sha=${env.TRACKING_JENKINS_FILE_COMMIT_BRANCH}")
    
   
    def responseJson = alm_Utilidades.jsonParse(response.content)
    def rcApi = response.toString().split(':')[1].toInteger()
    def prettyJSON = alm_Utilidades.jsonStr(response.content,true)

    //def listaCommit = [ sha: [], mensaje: []]
    //listaCommit.sha = responseJson[0].sha
    //listaCommit.mensaje = responseJson[0].commit.message

    if ( rcApi > 200 ) {
        alm_Utilidades.messages("Error en el servicio Github :${rcApi} \n Content:${prettyJSON}","error")
        env.TRACKING_QUERY_RESULT_DETAILS =prettyJSON
        error()
    }

    return [responseJson[0].sha , responseJson[0].commit.message]

}

def getTopics() {
    /*
    inputs:
        -> env.GIT_ORG
        -> env.GIT_NOMBRE_REPO
        -> env.GIT_API_BASE
    outputs:
        <- ArrayList con los topics del Repositorio
    */    
    def response = httpRequest(
        authentication: env.GIT_CRED,
        quiet: (env.DEBUG == 'true' ? false : true),
        validResponseCodes: "100:599",
        customHeaders: [
            [ name: 'Accept', value: 'application/vnd.github.mercy-preview+json', maskValue: false ]
        ],
        url: "${env.GIT_API_BASE}/repos/${env.GIT_ORG}/${env.GIT_NOMBRE_REPO}/topics")
    // println("Content: "+response.content)

    def topics = alm_Utilidades.jsonParse(response.content)

    alm_Debug.printJSON( "Return galGitHub.getTopics( ${env.GIT_NOMBRE_REPO} )" , topics.names )
    return topics.names
}

def putTopics() {
    /*
    inputs:
        -> env.GIT_ORG
        -> env.GIT_NOMBRE_REPO
        -> env.GIT_API_BASE
        -> env.GIT_TOPICS:  [opcion2, opcion, lp-cbl-v1]
    */

    if ( env.USER_ALM ) {
        println('***********************************************************************************')
        println('***** def putTopics  ******')
        println("***** env.GIT_TOPICS:${env.GIT_TOPICS}")
        println("***** env.GIT_ORG:${env.GIT_ORG}")
        println("***** env.GIT_NOMBRE_REPO:${env.GIT_NOMBRE_REPO}")
        println("***** env.GIT_API_BASE:${env.GIT_API_BASE}")
        println('***********************************************************************************')
    }

    def topicsArray = []

    if (env.GIT_TOPICS) {
        // Elimina los corchetes, separa por coma y limpia espacios
        topicsArray = env.GIT_TOPICS
            .replace('[','')
            .replace(']','')
            .split(',')
            .collect { it.trim() }
    } else {
        error "env.GIT_TOPICS está vacío o no definido."
    }

    if (topicsArray instanceof ArrayList && topicsArray.size() > 0) {
        def topics = [ names: topicsArray ]
        String bodyTopics = alm_Utilidades.jsonStr(topics)

        def httpResponse = httpRequest(
            authentication: env.GIT_CRED,
            quiet: (env.DEBUG == 'true' ? false : true),
            validResponseCodes: '100:599',
            customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github.mercy-preview+json', maskValue: false ]
            ],
            httpMode: 'PUT',
            requestBody: bodyTopics,
            url:"${env.GIT_API_BASE}/repos/${env.GIT_ORG}/${env.GIT_NOMBRE_REPO}/topics"
        )

        def prettyJSON = alm_Utilidades.jsonStr(httpResponse.content, true)

        if (httpResponse.status != 200) {
            alm_Utilidades.messages(
                "Error No se pudo etiquetar el repositorio, RC:${httpResponse.status} \n Content:${prettyJSON} \n Verifique que exista usuario 'udevo01m' como colaborador Role: 'Maintain'. ${env.GIT_BASE_URL}/${env.GIT_ORG}/${env.GIT_NOMBRE_REPO}/settings/access",
                "error"
            )
            env.TRACKING_QUERY_RESULT_DETAILS = prettyJSON
            sh 'error()'
        }
    } else {
        error "env.GIT_TOPICS no contiene valores válidos: ${env.GIT_TOPICS}"
    }
}


def readJsonFile(){
    /*
    leer un archivo Json dentro de un repo Github
    inputs:
        -> env.GIT_ORG
        -> env.GIT_ALM_CONFIG_REPO
        -> env.GIT_API_BASE
        -> env.GIT_ALM_CONFIG_TAG
        -> env.DEPLOY_JSON 
    outputs:
        <- objeto JSON
    */
    
    if ( env.USER_ALM ) {
        println('***********************************************************************************')
        println('***** readJsonFile() ******')
        println("***** env.GIT_ORG             : ${env.GIT_ORG} ")
        println("***** env.GIT_ALM_CONFIG_REPO  : ${env.GIT_ALM_CONFIG_REPO}")
        println("***** env.GIT_ALM_CONFIG_TAG   : ${env.GIT_ALM_CONFIG_TAG} ")
        println("***** env.DEPLOY_JSON          : ${env.DEPLOY_JSON}" )
        println('***********************************************************************************')
    }
    
    
    def url = "${env.GIT_API_BASE}/repos/${env.GIT_ORG}/${env.GIT_ALM_CONFIG_REPO}/contents/${env.DEPLOY_JSON}?ref=${env.GIT_ALM_CONFIG_TAG}"
    //println "url:${url}"
    def httpResponse = httpRequest(
        httpMode: 'GET',
        authentication: env.GIT_CRED,
        quiet: (env.DEBUG == 'true' ? false : true),
        url: url,
        customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github+json', maskValue: false ]
            ],
        validResponseCodes: "100:500",
        contentType: 'APPLICATION_JSON'
    )
    status = httpResponse.status
    if ( status != 200) {
        alm_Utilidades.messages("Error No se puede descargar del repositorio el archivo, \n RC:${status} \n Repo : ${env.GIT_ALM_CONFIG_TAG} \n File: ${env.DEPLOY_JSON}","error")
        def prettyJSON = alm_Utilidades.jsonStr(httpResponse.content,true)
        env.TRACKING_QUERY_RESULT_DETAILS =prettyJSON
        sh 'error()'
    }
    def json = readJSON text: httpResponse.content
    if (json.encoding != 'base64') {
        error "❌ El contenido no está en Base64. No se puede decodificar."
    }
    def cleanBase64 = json.content.replaceAll("\\s", "") // elimina espacios, \n, \r
    def decodedBytes = Base64.decoder.decode(cleanBase64)
    def decodedText = new String(decodedBytes, 'UTF-8')
    //def jsonFinal = readJSON text: decodedText
    // se valida que el json este bien armado.
    def jsonFinal
    try {
        jsonFinal = alm_Utilidades.jsonParse(decodedText)
    } catch (Exception e) {
        error "❌ Error de sintaxis en el JSON '${env.DEPLOY_JSON}': ${e.message}"
        env.TRACKING_QUERY_RESULT_DETAILS ="Error de sintaxis en el JSON '${env.DEPLOY_JSON}': ${e.message}"
        sh 'error()'
    }
    return jsonFinal
}




def getFile(){
    /*
    leer un archivo Json dentro de un repo Github
    inputs:
        -> env.GIT_ORG
        -> env.GIT_ALM_CONFIG_REPO
        -> env.GIT_API_BASE
        -> env.GIT_ALM_CONFIG_TAG

    outputs:
        <- archivo descargado
    */        
   if ( env.USER_ALM ) {
        println('***********************************************************************************')
        println('***** def getFile()  ******')
        println("***** env.GIT_ORG              : ${env.GIT_ORG} ******")
        println("***** env.GIT_ALM_CONFIG_REPO  : ${env.GIT_ALM_CONFIG_REPO} ******")
        println("***** env.GIT_ALM_CONFIG_TAG   : ${env.GIT_ALM_CONFIG_TAG} ******")
        println("***** env.DEPLOY_FILE          : ${env.DEPLOY_FILE} ******")
        println('***********************************************************************************')
    }
    def url = "${env.GIT_API_BASE}/repos/${env.GIT_ORG}/${env.GIT_ALM_CONFIG_REPO}/contents/${env.DEPLOY_FILE}?ref=${env.GIT_ALM_CONFIG_TAG}"
    def httpResponse = httpRequest(
        httpMode: 'GET',
        authentication: env.GIT_CRED,
        quiet: (env.DEBUG == 'true' ? false : true),
        url: url,
        customHeaders: [
                [ name: 'Accept', value: 'application/vnd.github+json', maskValue: false ]
            ],
        validResponseCodes: "100:500",
        contentType: 'APPLICATION_JSON'
    )
    status = httpResponse.status
    if ( status != 200) {
        alm_Utilidades.messages("Error No se puede descargar del repositorio el archivo, \n RC:${status} \n Repo : ${env.GIT_ALM_CONFIG_TAG} \n File: ${env.DEPLOY_JSON}","error")
        def prettyJSON = alm_Utilidades.jsonStr(httpResponse.content,true)
        env.TRACKING_QUERY_RESULT_DETAILS =prettyJSON
        sh 'error()'
    }
    def json = readJSON text: httpResponse.content
    if (json.encoding != 'base64') {
        error "❌ El contenido no está en Base64. No se puede decodificar."
    }
    def cleanBase64 = json.content.replaceAll("\\s", "") // elimina espacios, \n, \r
    def decodedBytes = Base64.decoder.decode(cleanBase64)
    def decodedText = new String(decodedBytes, 'UTF-8')

    def archFormat = env.DEPLOY_FILE.tokenize('/').last()            
    writeFile file: archFormat , text: decodedText
    alm_Utilidades.messages("SE descargo el archivo : ${archFormat}","info")
}

//-------------------------------
/*
 * Descarga un archivo o el contenido completo de una carpeta de un repositorio Git para Ansible de forma recursiva.
    input:
    -> env.ANS_COMPONENTE_DOWNLOAD         Componente a descargar del repositorio Git.
    -> isInitialCall                       Flag de primera vez dentro de la rutina.
    -> downloadStats                       Se utiliza para reporte al final de la rutina.
    output:
 */
def downloadAnsibleRaw(String componentPath, boolean isInitialCall = true, Map downloadStats = null, String branch = 'master') {
    if (isInitialCall) {
        downloadStats = [fileCount: 0, folderCount: 0]
        if (env.USER_ALM) {
            println '***********************************************************************************'
            println '***** downloadAnsibleRaw ******'
            println "***** Ruta del componente               : ${componentPath}"
            println "***** Base de la API Git                : ${env.GIT_API_BASE}"
            println "***** Organización                      : ${env.ANS_ORGA}"
            println "***** Repositorio Ansible               : ${env.ANS_REPO_ANSIBLE}"
            println "***** Rama                              : ${branch}"
            println '***********************************************************************************'
        }
    }

    def allItems = []
    def maxPages = 100
    def pageCount = 0
    def encodedPath = encodePath(componentPath)
    def nextUrl = buildApiUrl(encodedPath, branch)
    // Paginación de resultados de la API
    while (nextUrl && pageCount < maxPages) {
        def response = performHttpRequest(nextUrl)
        if (response.status != 200) {
            if (response.status == 404) {
                errorMsg = "Error Archivo :${componentPath} NO EXISTE"
            }else{
                errorMsg = "Error HTTP ${response.status} al consultar la ruta: ${componentPath}"
            }
            alm_Utilidades.messages(errorMsg, "error")
            env.TRACKING_QUERY_RESULT_DETAILS = alm_Utilidades.jsonStr(response.content ?: '', true)
            error(errorMsg)
        }
        def responseJson = alm_Utilidades.jsonParse(response.content)
        if (responseJson instanceof List) {
            allItems.addAll(responseJson)
        } else {
            allItems = responseJson
            break
        }
        // Manejo de paginación
        def linkHeader = response.headers['Link']?.find { it }
        nextUrl = null
        if (linkHeader) {
            println "entro linkHeader"
            def links = linkHeader.split(',').collect { it.trim() }
            def nextLink = links.find { it.contains('rel="next"') }
            if (nextLink) {
                nextUrl = (nextLink =~ /<([^>]+)>/)[0][1]
                println "hay paginado************************************************************************"
                println "hay paginado************************************************************************"
                println "hay paginado************************************************************************"
            }
        }
        pageCount++
        println "pageCount:${pageCount}"
    }
    // Procesar los elementos obtenidos
    if (allItems instanceof Map && allItems.type == 'file') {
        // Descargar archivo
        //def pathComponents = componentPath.split('/')
        //def modifiedPath = (pathComponents.size() > 1) ? pathComponents[1..-1].join('/') : componentPath
        //def localPath = "${env.JOB_BASE_NAME}/${modifiedPath}"
        def localPath = componentPath
        
        def downloadUrl = buildDownloadUrl(encodedPath, branch)
        def fileResponse = performHttpRequest(downloadUrl, localPath)

        if (fileResponse.status != 200) {
            def errorMsg = "Error al descargar el archivo ${localPath}: HTTP ${fileResponse.status}"
            alm_Utilidades.messages(errorMsg, "error")
            env.TRACKING_QUERY_RESULT_DETAILS = alm_Utilidades.jsonStr(fileResponse.content ?: '', true)
            error(errorMsg)
        }

        downloadStats.fileCount++
        alm_Utilidades.messages("Archivo descargado exitosamente: ${localPath}", "success")
    } else if (allItems instanceof List) {
        // Procesar carpeta recursivamente
        if (allItems.isEmpty()) {
            alm_Utilidades.messages("La carpeta ${componentPath} está vacía", "info")
        } else {
            downloadStats.folderCount++
            for (item in allItems) {
                String subPath = "${componentPath}/${item.name}"
                downloadAnsibleRaw(subPath, false, downloadStats, branch)
            }
            alm_Utilidades.messages("Carpeta procesada exitosamente: ${componentPath}", "info")
        }
    } else {
        def errorMsg = "Tipo desconocido en la ruta ${componentPath}, abortando"
        alm_Utilidades.messages(errorMsg, "error")
        error(errorMsg)
    }

    // Imprimir resumen en la primera llamada
    if (isInitialCall) {
        alm_Utilidades.messages("Resumen de la descarga: ${downloadStats.fileCount} archivo(s) descargado(s), ${downloadStats.folderCount} carpeta(s) procesada(s)", "info")
    }
}


def encodePath(String path) {
    // Función auxiliar de downloadAnsibleRaw para codificar URLs de manera segura sin regex
    String encoded = path
    def replacements = [
        ' ': '%20',
        '(': '%28',
        ')': '%29'
    ]
    replacements.each { src, dest -> encoded = encoded.replace(src, dest) }
    return encoded
}
// Función auxiliar de downloadAnsibleRaw 
def buildApiUrl(String encodedPath, String referencia) {
    "${env.GIT_API_BASE}/repos/${env.ANS_ORGA}/${env.ANS_REPO_ANSIBLE}/contents/${encodedPath}?ref=${referencia}"
}
// Función auxiliar de downloadAnsibleRaw 
def buildDownloadUrl(String encodedPath, String referencia) {
    "${env.GIT_BASE_URL}/raw/${env.ANS_ORGA}/${env.ANS_REPO_ANSIBLE}/${referencia}/${encodedPath}"
}

def performHttpRequest(String url, String outputFile = null) {
    //println "performHttpRequest: ${url}"
    def requestParams = [
        httpMode: 'GET',
        authentication: env.GIT_CRED,
        quiet: (env.DEBUG == 'true' ? false : true),
        url: url,
        customHeaders: [[name: 'Accept', value: 'application/vnd.github.v3+json', maskValue: false]],
        validResponseCodes: "100:500"
    ]
    if (outputFile) {
        requestParams.outputFile = outputFile
    }
    def response = httpRequest(requestParams)
    return response
}