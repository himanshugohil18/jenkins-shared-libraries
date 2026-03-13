def call(String sonarName, String projectKey, String projectName) {
    withSonarQubeEnv(sonarName) {
        sh """
        ${tool 'Sonar'}/bin/sonar-scanner \
        -Dsonar.projectKey=${projectKey} \
        -Dsonar.projectName=${projectName} \
        -Dsonar.sources=.
        """
    }
}
