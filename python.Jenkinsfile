#!groovy

// Cargar librerías
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

// Declarative Pipeline estándar
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
        stage('Setup Piper') {
            steps {
                // Inicializa el entorno de Piper
                setupCommonPipelineEnvironment script: this
            }
        }

        stage('Clone Repo') {
            steps {
                echo "Clonando repositorio..."
                sh 'git clone https://github.com/mapenagames/sapcloudGAL.git'
                sh '''
                    cd sapcloudGAL/python
                    pwd
                    ls -la
                '''
            }
        }

        stage('Ejecutar en Python 3.10') {
            steps {
                withDockerContainer(image: 'python:3.10') {
                    sh '''
                        cd sapcloudGAL/python
                        pwd
                        ls -all
                        python --version
                        pip install requests
                        python -c "import requests; print(requests.__version__)"
                    '''
                }
            }
        }

        stage('Ejecutar en Python 3.10  b') {
            steps {
                echo "Iniciando contenedor Python 3.10..."
                dockerExecute(
                    script: this,
                    dockerImage: 'python:3.10'
                ) {
                    sh '''
                        echo "=== Workspace en contenedor ==="
                        cd sapcloudGAL/python
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
            echo "Pipeline finalizado."
            // cleanWs()
        }
    }
}