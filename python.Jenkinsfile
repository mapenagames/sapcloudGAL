#!groovy
//env.GIT_CRED = "GitHubPusher" //Credenciales
env.GIT_BASE_URL = "https://github.com" //URL Git
env.GIT_API_BASE = "https://api.github.com" 
env.GIT_BRANCH = "main"
env.GIT_TAG = ""
env.GIT_ORG = "mapenagames"
env.GIT_NOMBRE_REPO = "sapcloudGAL"
//library(
//    changelog: false,
//    identifier: 'piper-lib-os@master',
//    retriever: modernSCM([
//        $class: 'GitSCMSource',
//        remote: "https://github.com/SAP/jenkins-library.git"
//        //remote: "https://github.bancogalicia.com.ar/alm/jenkins-library.git"
//
//    ])
//)


library(
    changelog: false,
    identifier: 'piper-lib-os@v1.470.0',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: "https://github.com/SAP/jenkins-library.git"
    ])
)

library(
    changelog: false,
    identifier: 'alm@main',
    retriever: modernSCM(
        [
            $class: 'GitSCMSource',
            //credentialsId: env.GIT_CRED,
            remote: "https://github.com/mapenagames/sapcloudGAL"
        ]
    )
)

node() {
    stage('clone Repo') {
        script {
            cleanWs()
            alm_VarsEnv()
            alm_Utilidades.logRotator()
            println "stage 1"
            //alm_GitHub.cloneRepo(@
            sh " git clone https://github.com/mapenagames/sapcloudGAL.git"
            sh """
               cd sapcloudGAL/python
               pwd
               ls -all
            """
        }

    }
    stage('Run FastAPI Hola Mundo') {
        //env.PIPER_stageName = 'Run FastAPI'
        script {
    
            println "docker execute"
            //    dockerExecute(script: this, dockerImage: 'python:3.10') {
            //        sh '''
            //            
            //            cd sapcloudGAL/python
            //            pip install -r requirements.txt
            //            uvicorn app:app --host 0.0.0.0 --port 8000
            //        '''
        }
    }
    stage('Ejecutar en Python 3.10') {
        script {
            println "docker execute2"
            dockerExecute(
                script: this,
                dockerImage: 'python:3.10'
            ) {
                sh '''
                pwd
                ls -all
                '''
                sh 'python --version'
                sh 'pip install requests'
                sh 'python -c "import requests; print(requests.__version__)"'
            }
        }
    }
}




