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
     
        }

        stage('Checkstyle') {
            steps {
                bat 'mvn checkstyle:check'
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
