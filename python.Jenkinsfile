#!groovy
// Cargar librerías
library(
    identifier: 'piper-lib-os@v1.470.0',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: 'https://github.com/SAP/jenkins-library.git'
    ])
)
pipeline {
    agent any
    stages {
        stage('Setup Piper') {
            steps {
                // Inicializa el entorno de Piper
                setupCommonPipelineEnvironment script: this
            }
        }
        stage('Ejecutar en Python 3.10') {
            steps {
                withDockerContainer(image: 'python:3.10') {
                    sh '''
                        pwd
                        ls -all
                        python --version
                        pip install requests
                        python -c "import requests; print(requests.__version__)"
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