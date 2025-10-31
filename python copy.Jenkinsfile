#!groovy

// ============================================================
// Cargar las librerías compartidas antes del bloque pipeline
// ============================================================

library(
    identifier: 'piper-lib-os@v1.470.0',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: 'https://github.com/SAP/jenkins-library.git'
    ])
)

library(
    identifier: 'alm@main',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: 'https://github.com/mapenagames/sapcloudGAL.git'
    ])
)
// ============================================================
// Declarative Pipeline
// ============================================================
pipeline {
    agent any

    environment {
        GIT_BASE_URL     = "https://github.com"
        GIT_API_BASE     = "https://api.github.com"
        GIT_BRANCH       = "main"
        GIT_ORG          = "mapenagames"
        GIT_NOMBRE_REPO  = "sapcloudGAL"
    }

    stages {
        stage('Ejecutar en Python 3.10') {
            steps {
                echo "Iniciando contenedor Python 3.10..."
                dockerExecute(
                    script: this,
                    dockerImage: 'python:3.10'
                ) {
                    sh '''
                        echo "=== Workspace en contenedor ==="
                        pwd
                        ls -la

                        echo "=== Python version ==="
                        python --version

                        echo "=== Instalando requests ==="
                        pip install --no-cache-dir requests

                        echo "=== Versión de requests ==="
                        python -c "import requests; print('requests version:', requests.__version__)"
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                println "fin"
            //cleanWs()
            }
        }
    }
}
