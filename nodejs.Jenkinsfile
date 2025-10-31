#!groovy
//env.GIT_CRED = "GitHubPusher" //Credenciales
library(
    changelog: false,
    identifier: 'piper-lib-os@master',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        //credentialsId: env.GIT_CRED,
        //remote: "https://github.com/SAP/jenkins-library.git"
        remote: "https://github.bancogalicia.com.ar/alm/jenkins-library.git"
    ])
)

node("all") {
    stage('Run Node.js Hola Mundo') {
        env.PIPER_stageName = 'Run Node'
        script {
            dockerExecute(script: this, dockerImage: 'node:18') {
                sh '''
                    npm install
                    npm start
                '''
            }
        }
    }
}
