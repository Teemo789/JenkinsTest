pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE = 'teemo/spring-boot-app'
        DOCKER_TAG = 'latest'
        MAVEN_VERSION = 'Maven'
        DOCKER_CREDENTIALS = 'docker-credentials'
        SONARQUBE_SERVER = 'SonarQube'
    }
    tools {
        maven "${MAVEN_VERSION}"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Maven Compile') {
            steps {
                script {
                    echo 'Compiling project with Maven...'
                    bat './mvnw clean compile'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv("${SONARQUBE_SERVER}") {
                        bat './mvnw sonar:sonar'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    echo 'Building Spring Boot application...'
                    bat './mvnw clean package -DskipTests'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    bat "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to registry...'
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        bat "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD} ${DOCKER_REGISTRY}"
                        bat "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                node {
                    echo 'Cleaning up workspace...'
                    cleanWs()
                }
            }
        }
        success {
            echo 'Pipeline executed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
