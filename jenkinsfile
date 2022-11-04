pipeline {
    agent any

    stages {
        stage('build'){
            steps{
                sh '''
                    echo build start
                    ./gradlew clean bootJar
                '''
            }
        }

        stage('run sonarqube'){
            steps{
                sh '''
                ./gradlew sonarqube \
                -Dsonar.projectKey=hamkke \
                -Dsonar.host.url=http://43.201.18.36:8080 \
                -Dsonar.login=sqp_e4eddaeb8835c21270460700da5e60e427745543
                '''
            }
        }
    }
}
