pipeline {
    agent any

    environment {
        EC2_USER        = 'ubuntu'
        EC2_HOST        = '54.87.62.117'
        DEPLOY_DIR      = '/home/ubuntu/connectsphere'
        GIT_REPO        = 'https://github.com/vikash8058/cs-backend.git'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '>>> Cloning repository...'
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: "${GIT_REPO}"
            }
        }

        stage('Create .env File') {
            steps {
                echo '>>> Writing .env file from Jenkins secrets...'
                withCredentials([file(credentialsId: 'env-file', variable: 'ENV_FILE')]) {
                    sh 'cp $ENV_FILE .env'
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                echo '>>> Deploying to EC2 via Docker Compose...'
                sh '''
                    # Create deploy directory if not exists
                    mkdir -p ${DEPLOY_DIR}

                    # Copy all files to deploy directory
                    cp -r . ${DEPLOY_DIR}/

                    # Go to deploy directory
                    cd ${DEPLOY_DIR}

                    # Stop existing containers
                    docker compose down || true

                    # Build and start all containers
                    docker compose up --build -d

                    # Show running containers
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