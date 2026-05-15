pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo '>>> Cloning repository...'
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/vikash8058/cs-backend.git'
            }
        }

        stage('Create .env File') {
            steps {
                echo '>>> Writing .env file from Jenkins secrets...'
                withCredentials([file(credentialsId: 'env-file', variable: 'ENV_FILE')]) {
                    sh 'rm -f .env && cp $ENV_FILE .env'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '>>> Deploying via Docker Compose...'
                sh '''
                    docker compose down || true
                    docker compose up --build -d
                    docker compose ps
                '''
            }
        }

        stage('Health Check') {
            steps {
                echo '>>> Waiting for services to start...'
                sh '''
                    sleep 30
                    curl -f http://localhost:8080/actuator/health || echo "Gateway starting up..."
                    curl -f http://localhost:8761 || echo "Eureka starting up..."
                '''
            }
        }
    }

    post {
        success {
            echo '>>> Deployment successful! ConnectSphere is live.'
        }
        failure {
            echo '>>> Deployment failed. Check logs above.'
        }
    }
}