# sapcloudGAL
dockerExecute(
    script: this,
    dockerImage: 'python:3.10',
    dockerVolumeMounts: ['/home/user/.cache:/root/.cache'],  // Montar volúmenes
    dockerEnvVars: ['ENV=production'],                       // Variables de entorno
    dockerWorkdir: '/app',                                   // Directorio de trabajo
    reuseContainer: true                                     // Reusar contenedor (útil para depuración)
) {
    // tus pasos aquí
}

        stage('Run FastAPI Hola Mundo') {
            steps {
                echo "Preparando FastAPI..."
                // 🔸 Descomenta si querés ejecutar la app en un contenedor Python
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

        stage('Clone Repo') {
            steps {
                cleanWs()
                echo "Clonando repositorio..."
                sh 'git clone https://github.com/mapenagames/sapcloudGAL.git'

                sh '''
                    cd sapcloudGAL/python
                    pwd
                    ls -la
                '''
            }
        }



pipe
library(
    identifier: 'alm@main',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: 'https://github.com/mapenagames/sapcloudGAL.git'
    ])
)

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