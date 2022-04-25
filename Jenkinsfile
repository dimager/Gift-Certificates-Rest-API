node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    withSonarQubeEnv() {
      sh "chmod +x ./gradlew"
      sh "./gradlew jacocoTestReport"
      sh "./gradlew sonarqube"
    }
  }
}