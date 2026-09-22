pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')  // Reference to DockerHub credentials in Jenkins
        DOCKERHUB_REPO = 'futureskilldockerhub/olist-service'           // DockerHub repository
        IMAGE_VERSION_FILE = 'VERSION'                                    // File that contains the current version
    }

    stages {


        stage('Increment Version') {
            steps {
                script {
                    // Read and increment version number (Major.Minor)
                    def version = readFile(IMAGE_VERSION_FILE).trim()
                    def (major, minor) = version.tokenize('.').collect { it as int }
                    minor++  // Increment Minor Version
                    def newVersion = "${major}.${minor}"
                    writeFile file: IMAGE_VERSION_FILE, text: newVersion
                    echo "New version: ${newVersion}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def newVersion = readFile(IMAGE_VERSION_FILE).trim()
                    // Build the Docker image and tag it with the new version
                    sh "docker build -t ${DOCKERHUB_REPO}:${newVersion} ."
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    def newVersion = readFile(IMAGE_VERSION_FILE).trim()
                    // Log in to DockerHub and push the image
                    sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
                    sh "docker push ${DOCKERHUB_REPO}:${newVersion}"
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    // Run docker-compose to deploy the application
                    def newVersion = readFile(IMAGE_VERSION_FILE).trim()
                    sh """
                    docker-compose down
                    sed -i 's|image: .*|image: ${DOCKERHUB_REPO}:${newVersion}|' docker-compose.yml
                    docker-compose up -d
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
