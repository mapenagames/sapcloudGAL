#!groovy
env.GIT_BASE_URL = "https://github.com"
env.GIT_API_BASE = "https://api.github.com"
env.GIT_BRANCH = "main"
env.GIT_ORG = "mapenagames"
env.GIT_NOMBRE_REPO = "sapcloudGAL"

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
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: "https://github.com/mapenagames/sapcloudGAL"
    ])
)

node() {
    stage('Clone Repo') {
        steps {
            cleanWs()
            // alm_VarsEnv()  // ← Descomenta si existe
            // alm_Utilidades.logRotator()
            echo "Clonando repositorio..."
            sh 'git clone https://github.com/mapenagames/sapcloudGAL.git'
            sh '''
                cd sapcloudGAL/python
                pwd
                ls -la
            '''
        }
    }

    stage('Run FastAPI Hola Mundo') {
        steps {
            echo "Preparando FastAPI..."
            // Descomenta cuando quieras ejecutar
            /*
            dockerExecute(
                script: this,
                dockerImage: 'python:3.10'
            ) {
                sh '''
                    cd sapcloudGAL/python
                    pip install -r requirements.txt
                    uvicorn app:app --host 0.0.0.0 --port 8000 --reload
                '''
            }
            */
        }
    }

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