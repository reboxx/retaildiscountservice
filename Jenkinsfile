pipeline {
    agent any
    
    tools {
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/reboxx/retaildiscountservice.git', 
                    credentialsId: 'github-token'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco execPattern: '**\\target\\jacoco.exec',
                           classPattern: '**\\target\\classes',
                           sourcePattern: '**\\src\\main\\java'
                }
            }
        }

        stage('Checkstyle') {
            steps {
                bat 'mvn checkstyle:check'
            }
        }

        stage('SonarQube Analysis') {
    		when {
        		environment name: 'SONAR_HOST_URL', value: '' // runs only if SONAR_HOST_URL is set
    		}
    		steps {
        		bat 'mvn sonar:sonar'
    		}
        }
    }

    post {
        always {
            echo 'Pipeline finished'
        }
        success {
            echo 'Build, tests, coverage, and analysis succeeded!'
        }
        failure {
            echo 'One or more stages failed!'
        }
    }
}
