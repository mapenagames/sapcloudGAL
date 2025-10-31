#!groovy
// Cargar librerías
//library(
//    identifier: 'piper-lib-os@v1.470.0',
//    retriever: modernSCM([
//        $class: 'GitSCMSource',
//        remote: 'https://github.com/SAP/jenkins-library.git'
//    ])
//)
piperPipeline {
    agent any

    stages {
        stage('Ejecutar en Python 3.10') {
            steps {
                dockerExecute(
                    script: this,
                    dockerImage: 'python:3.10'
                ) {
                    sh '''
                        echo "=== Python en contenedor ==="
                        python --version
                    '''
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline finalizado."
        }
    }
}