
def call() {
   //general

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

    //jenkins
    env.JENKINS_USER_APPROVER = "n/a" 
    env.JENKINS_LOG_ROTATOR = 5                    // define la cantidad de ejecuciones que guarda jenkins
    env.JENKINS_DATE_START = env.tdatealm
    env.JENKINS_DATE_END = ""
    env.JENKINS_LAST_STAGE = env.STAGE_NAME
    env.JENKINS_BUILD_USER_ID = wrap([$class: 'BuildUser']) { return env.JENKINS_BUILD_USER_ID }
    


    
}
