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
