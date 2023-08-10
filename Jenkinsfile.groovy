pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = "flask-hello-world"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${flask-hello-world}:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Push to Docker Registry') {
            steps {
                script {
                    docker.withRegistry('https://registry.example.com', 'docker-registry-credentials') {
                        docker.image("${flask-hello-world}:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                docker.image("${flask-hello-world}:${env.BUILD_NUMBER}").remove()
            }
        }
    }
}
