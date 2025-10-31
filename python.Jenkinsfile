#!groovy

// ¡NO uses library(...) aquí!
// La librería ya está cargada como Global Library

piperPipeline {
    agent any

    stages {
        stage('Ejecutar Python') {
            steps {
                dockerExecute(
                    script: this,
                    dockerImage: 'python:3.10'
                ) {
                    sh '''
                        echo "=== Python en contenedor ==="
                        python --version
                        pip install --no-cache-dir requests
                        python -c "import requests; print('requests:', requests.__version__)"
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