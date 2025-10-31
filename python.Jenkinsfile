#!groovy
//env.GIT_CRED = "GitHubPusher" //Credenciales
env.GIT_BASE_URL = "https://github.com" //URL Git
env.GIT_API_BASE = "https://api.github.com" 
env.GIT_BRANCH = "main"
env.GIT_TAG = ""
env.GIT_ORG = "mapenagames"
env.GIT_NOMBRE_REPO = "sapcloudGAL"
library(
    changelog: false,
    identifier: 'piper-lib-os@master',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: "https://github.com/SAP/jenkins-library.git"
        //remote: "https://github.bancogalicia.com.ar/alm/jenkins-library.git"

    ])
)
node() {
    stage('clone Repo') {
        script {
            println "stage 1"
            alm_GitHub.cloneRepo()
            sh 'pwd'
            sh 'ls -all'
        }
    }
    //stage('Run FastAPI Hola Mundo') {
    //    env.PIPER_stageName = 'Run FastAPI'
    //    script {
    //        dockerExecute(script: this, dockerImage: 'python:3.10') {
    //            sh '''
    //                pip install -r requirements.txt
    //                uvicorn app:app --host 0.0.0.0 --port 8000
    //            '''
    //        }
    //    }
    //}
}




