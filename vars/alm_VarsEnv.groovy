
def call() {
   //general
    env.TOOLKIT_VER = "V666"
    //env.APP_SIGLA = "${JOB_NAME}".split('/')[0].split('-')[0].toLowerCase()
    env.APP_SIGLA = getApp()
    env.APP_PNAME =  ""                              // de define mas abajo
    env.APP_LANG_PROG = ""                   // Identifica el lenguaje de programacion del repo
    env.APP_MOTOR_BD = ""                             // Identifica el motor de bases de datos del repo
    env.APP_COMPILAR_DESDE_TAG_BRANCH = ""           // identifica si la fuente a compilar es BRANCH o TAG
    env.APP_INFRA = "infra.json"                     // nombre del archivo donde se declara la infra de la app, ej Server, base de datos,etc
    env.APP_DEPLOYPATH = "deploypath.json"           // nombre del archivo donde se declara el deployPath del repo, ej Desarrollo,Integracion,produccion,etc
                                                     // alm_config/demopy/deploypath.json
    env.APP_AMBIENTES = ""                           // lista de ambientes de la aplicacion/repo. ver archivo deploypath.json en alm_config
    env.APP_ACCION = "."                              // seleccionar menu principal
    //Deploy
    env.DEPLOY_ENV = ""                              // define a que ambiente se deploya
    env.DEPLOY_JSON = ""                             // representa el nombre del archivo json donde se declara el destino del despliegue (path completo del arhivo infra.json)
                                                     // ej: alm_config/env.GIT_NOMBRE_REPO/DEPLOY_ENV/env.APP_INFRA"
    
    env.DEPLOY_FILE = ""                             // representa el archivo a descargar
    env.DEPLOY_SERVER = ""                           // lista de server a deployar
    env.DEPLOY_TIPO = "WAR"                          // IDENTIFICA EL TIPO DE DEPLOY PARA DEFINIR EL PATHS DE INSTALACION
    
    //Ansible
    env.ANS_ORGA = "alm"                            // nombre de la organizacion donde estan los playbooks
    env.ANS_REPO_ANSIBLE = "ansible_deploy"         // nombre del repositorio de/los playbook
    env.ANS_COMPONENTE_DOWNLOAD = ""                // nombre del componente a descargar
    env.ANS_INVENTARIO = "inventory"                // nombre del archivo hosts (inventario) para el ansible
    env.ANS_VAULT = "allServer"                     // nombre del vault
    env.ANS_SERVER_SO = ""                          // nombre del sistema operativo del server
    env.ANS_SERVER = ""                             // nombre del servidor ANSible a procesar
    env.ANS_OS_CATEGORIA = ""
    env.ANS_PLAYBOOK = ""                           // nombre del playbook a ejecutar
    // github
    env.GIT_API_BASE = "${env.GIT_BASE_URL}/api/v3" 
    env.GIT_ORG = ""                                // de define mas abajo
    env.GIT_BRANCH = ""                             // branch de repo github
    env.GIT_COMMIT = ""                             // commit de la branch / TAg
    env.GIT_TAG =""                                 // identifica el tag seleccionado de la aplicacion
    env.GIT_TOPICS =""                              // identifica los topics de repo app de github
    env.GIT_NOMBRE_REPO =""                         // identifica repo que se va a procesar
    env.GIT_NOMBRE_REPO_ID =""                      // identifica el ID del repo 
    env.GIT_NOMBRE_REPO_HTML_URL =""                // identifica LA URL del repo
    env.GIT_URL = ""                                // url utilizado para hacer git clone
    env.GIT_ALM_CONFIG_TAG =""                      // identifica el tag del repo de configuracion.
    env.GIT_ALM_CONFIG_CURLOMMIT =""                // identifica el commit del tag del repo de configuracion.
    env.GIT_ALM_CONFIG_REPO = "alm_config"          // nombre del repo de configuracion 
    env.GIT_ALM_CONFIG_REPO_ID = ""                 // id del repo de alm_config 
    env.GIT_ALM_CONFIG_HTML_URL =""                 // identifica LA URL del repo alm_config
    env.GIT_ALM_CONFIG_PARAM = ""                   //identifica los parametros guardados en alm_config

    //nexus
    env.NEXUS_URL  = "http://lnexusapp01:8081"
    env.NEXUS_REPO = ""                             // Nombre del repo a gestionar
    env.NEXUS_FOLDER = ""                           // Nombre del Folder
    env.NEXUS_FILE   = ""                           // Nombre del archivo a procesar
    env.NEXUS_SHA256 = ""                           // representa el sha256 del archivo a descargar de nexus
    env.NEXUS_DATE_UPLOAD = ""                      // fecha de subida a nexus el componente
    
    //tracking
    env.TRACKING_JENKINS_FILE = ""                  //  ubicacion del jenkinsfile "toolsMF/Jenkinsfile"
    env.TRACKING_JENKINS_FILE_COMMIT = env.TRACKING_JENKINS_FILE
    env.TRACKING_JENKINS_FILE_COMMIT_REPO = "alm/damian"   // (env.GIT_ORG / REPO) ubicacion del jenkinsfile del env.TRACKING_JENKINS_FILE_COMMIT
    env.TRACKING_JENKINS_FILE_COMMIT_BRANCH = "master"     // Branch del arhivo a buscar el commit
    env.TRACKING_BASE = "https://tracking-alm6-desplieguealm-dev.apps.paas-dev.bancogalicia.com.ar"
    env.TRACKING_URL_INSERT = ""                    //  parte de url de tracking de cambio "/mf/insertar/"
    env.TRACKING_URL_BUSCAR = "/rm/buscar"
    
    env.TRACKING_QUERY_ENV = ""                      // ambiente para la consulta de /rm/buscar
    env.TRACKING_QUERY_RESULT_DETAILS = "n/a"
    env.TRACKING_QUERY_PIPE_RESULT   = "SUCCESS"    //  condición de busqueda en el tracking
    env.TRACKING_QUERY_RESULT  = ""                 //  guarda el resultado de la busqueda

    //jenkins
    env.JENKINS_USER_APPROVER = "n/a" 
    env.JENKINS_LOG_ROTATOR = 5                    // define la cantidad de ejecuciones que guarda jenkins
    env.JENKINS_DATE_START = env.tdatealm
    env.JENKINS_DATE_END = ""
    env.JENKINS_LAST_STAGE = env.STAGE_NAME
    env.JENKINS_BUILD_USER_ID = wrap([$class: 'BuildUser']) { return env.JENKINS_BUILD_USER_ID }
    env.JENKINS_PIPE_COMMIT = alm_GitHub.getLastCommitFile() 

    //changeman
    env.CMNA_BASE_URL = "http://bgal1.bancogalicia.com.ar:8090/zmfrest"
    env.CMNA_PARM_PKG_BACKOUT_RV = true
    env.CMNA_PARM_PKG_BAS_STATUS = ""
    env.CMNA_PARM_PKG_CREATOR = ""                  // creador del paquete changeman seleccionado 
    env.CMNA_PARM_PKG_FROZEN_STATUS = ""
    env.CMNA_PARM_PKG_ID = ""
    env.CMNA_PARM_PKG_ID_DISPLAY = ""
    env.CMNA_PARM_PKG_PROMOTION_LEVEL = ""
    env.CMNA_PARM_PKG_TITLE = ""                    // package title
    env.CMNA_PARM_PROMOTE_ENV = "homo"              // a que ambiente se hace el promote (DEV,HOMO)
    env.CMNA_PARM_SIGLA = "null"
    env.CMNA_PARM_SIGLAPESOS = "null"
    env.CMNA_PESOS = "%24"                          // reemplaza simbolo '$' en la api
    env.CMNA_WAIT_FOR_COMPLETION = "Y"              //espera a que el job mainframe termine para responder info del jobs
    env.CMNA_WAIT_TIMEOUT = 1                       //tiempo en minutos de espera a que termine el job de mainframe

    //Services Now
    env.SN_DATE_VEDA = ""                                                             // fecha de veda formato SN
    //  SN_DATE_VEDA =  aaaammddThhmmssZ
    //  SN_DATE_VEDA = "20240630T210004Z"
    env.SN_DEPLOY_ENV = ""                                                            // nombre ambiente en SN que se deploya ej: PRODUCCIÓN
    env.SN_HISTORIES = ""                                                             // historias relacionadas
    env.SN_INCI      = ""                                                             // incidente relacionado (solo 1 acepta SN)
    env.SN_DATE_VEDA_DISPLAY = ""                                                     // fecha de veda formato usuario
    env.SN_CHG_VEDA= "n/a"                                                            // change de Veda para el Salto de Veda
    env.SN_CHANGE = "n/a"                                                             // ID de Change que se genera para una implementacion
    env.SN_NOTA_OK =    "Implementación finalizada Correctamente : successful"        // seteo terminacion de change SN por OK
    env.SN_NOTA_NOTOK = "Implementación finalizada con Error     : unsuccessful"      // seteo terminacion de change SN por NOTOK
    env.SN_CHANGE_TEXT = ""                                                           //texto a agregar el el ticket alta de CHange por si se quiere agregar un dato mas.   
    //env.SN_BASE = "https://bancogaliciaprod.service-now.com"
    env.SN_BASE = "https://bancogaliciadev.service-now.com"
    env.SN_INFRA = ""                                                                 // valor a buscar la infra de la app ej: "Servidor" , "Base de datos"

    //Asignacion del nombre de aplicacion en SN.  
    env.APP_PNAME =  alm_ServiceNow.getPname()
    env.GIT_ORG = "${env.APP_SIGLA}-${env.APP_PNAME}"

    //roles
    env.AD_APPROVER_GROUP = "GAP${env.APP_SIGLA}${env.APP_PNAME}APPROVER"  
    env.AD_DEV_GROUP = "GAP${env.APP_SIGLA}${env.APP_PNAME}DEV"  
    env.AD_VIEWER_GROUP = "GAP${env.APP_SIGLA}${env.APP_PNAME}VIEWER"  
        
}

def getApp() {
    def parts = env.JOB_NAME.tokenize('/')
    def x = parts.size()
    if (parts.size() < 2) {
        alm_Utilidades.messages("Error en el nombre del Folder, no se puede determinar el nombre, job_name : ${env.JOB_NAME}","error")
        error()
    }
    def folderAnterior = parts[-2]
    def sigla = folderAnterior.contains("-") ? folderAnterior.split("-", 2)[0] : folderAnterior
    return sigla.toLowerCase()
}
