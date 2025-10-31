piperPipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                dockerExecute(script: this, dockerImage: 'python:3.10') {
                    sh 'python --version'
                }
            }
        }
    }
}