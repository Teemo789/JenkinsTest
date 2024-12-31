pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS_ID = 'docker-credentials'
        DOCKER_REPO = 'teemo/spring-boot-app'
        DOCKER_TAG = 'latest'
        REMOTE_SERVER = 'remote-user@remote-server-ip'
        REMOTE_SERVER_SSH = 'remote-server-ssh'
        SONARQUBE_ENV_NAME = 'SonarQube'
        SONARQUBE_PROJECT_KEY = 'teemo_spring_project'
    }
    tools {
        maven 'Maven'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean install'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONARQUBE_ENV_NAME) {
                    sh "./mvnw sonar:sonar -Dsonar.projectKey=${SONARQUBE_PROJECT_KEY}"
                }
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package'
            }
        }
        stage('Verify WAR File') {
            steps {
                script {
                    if (!fileExists('target/spring-boot-app-0.0.1-SNAPSHOT.war')) {
                        error 'WAR file not found. Build failed.'
                    }
                }
            }
        }
        stage('Building Docker Image') {
            steps {
                script {
                    appImage = docker.build("${DOCKER_REPO}:${DOCKER_TAG}")
                }
            }
        }
        stage('Pushing Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_CREDENTIALS_ID) {
                        appImage.push()
                    }
                }
            }
        }
        stage('Deploy to Remote Server') {
            steps {
                sshagent([REMOTE_SERVER_SSH]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $REMOTE_SERVER "
                    docker stop $(docker ps -q --filter ancestor=${DOCKER_REPO}:${DOCKER_TAG}) || true &&
                    docker rm $(docker ps -q --filter ancestor=${DOCKER_REPO}:${DOCKER_TAG}) || true &&
                    docker pull ${DOCKER_REPO}:${DOCKER_TAG} &&
                    docker run -d -p 8080:8080 ${DOCKER_REPO}:${DOCKER_TAG}
                    "
                    """
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs for details.'
        }
    }
}
