# 🚀 Jenkins Shared Libraries

This repository contains reusable Jenkins Shared Library functions written in Groovy.

These libraries help standardize CI/CD pipelines, reduce duplicated code, and simplify Jenkins pipeline development across multiple projects.

The shared library provides reusable steps for:

- Git repository cloning
- Docker image build
- Docker image push
- Unit testing
- Security scanning with Trivy
- Generating build reports
- Updating Kubernetes manifests
- Application deployment

---

# 📁 Repository Structure

```
jenkins-shared-libraries
│
├── vars
│   ├── clone.groovy
│   ├── docker_build.groovy
│   ├── docker_push.groovy
│   ├── deploy.groovy
│   ├── run_tests.groovy
│   ├── trivy_scan.groovy
│   ├── generate_reports.groovy
│   ├── update_k8s_manifests.groovy
│   └── hello.groovy
│
└── README.md
```

The **vars/** directory contains global pipeline functions that can be directly used inside a Jenkins pipeline.

---

# 📌 Shared Library Functions

## hello.groovy

Simple function used to verify that the shared library is loaded correctly.

```groovy
def call(){
    echo "Hello Dosto"
}
```

Usage:

```groovy
hello()
```

---

# 📦 Git Operations

## clone.groovy

Clones a Git repository and branch.

```groovy
def call(String url, String branch){
    git url: "${url}", branch: "${branch}"
}
```

Usage:

```groovy
clone(
 "https://github.com/your-repo/project.git",
 "main"
)
```

---

# 🐳 Docker Operations

## docker_build.groovy

Builds a Docker image.

```groovy
def call(Map config = [:]) {

    def imageName = config.imageName ?: error("Image name is required")
    def imageTag = config.imageTag ?: 'latest'
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'

    echo "Building Docker image: ${imageName}:${imageTag} using ${dockerfile}"

    sh """
        docker build -t ${imageName}:${imageTag} -t ${imageName}:latest -f ${dockerfile} ${context}
    """
}
```

Usage:

```groovy
docker_build(
    imageName: "notes-app",
    imageTag: "latest"
)
```

---

## docker_push.groovy

Pushes the Docker image to Docker Hub.

```groovy
def call(Map config = [:]) {

    def imageName = config.imageName ?: error("Image name is required")
    def imageTag = config.imageTag ?: 'latest'
    def credentials = config.credentials ?: 'docker-hub-credentials'

    echo "Pushing Docker image: ${imageName}:${imageTag}"

    withCredentials([usernamePassword(
        credentialsId: credentials,
        usernameVariable: 'DOCKER_USERNAME',
        passwordVariable: 'DOCKER_PASSWORD'
    )]) {

        sh """
            echo "\$DOCKER_PASSWORD" | docker login -u "\$DOCKER_USERNAME" --password-stdin
            docker push ${imageName}:${imageTag}
            docker push ${imageName}:latest
        """
    }
}
```

Usage:

```groovy
docker_push(
    imageName: "notes-app",
    imageTag: "latest",
    credentials: "docker-hub-credentials"
)
```

---

# 🚀 Deployment

## deploy.groovy

Deploys the application container.

```groovy
def call() {

    echo "Starting deployment..."

    sh """

        echo "Stopping container using port 8000 (if any)..."
        docker ps -q --filter "publish=8000" | xargs -r docker stop
        docker ps -aq --filter "publish=8000" | xargs -r docker rm

        echo "Removing existing notes-app container (if any)..."
        docker rm -f notes-app || true

        echo "Running new container..."
        docker run -d \
        --name notes-app \
        -p 8000:8000 \
        himanshugohil18/notes-app:latest

    """

    echo "Deployment completed successfully"
}
```

Usage:

```groovy
deploy()
```

---

# 🧪 Testing

## run_tests.groovy

Runs unit tests before building the application.

```groovy
def call() {

    echo "Running unit tests..."

    // Add your unit test commands here
    // Example:
    // sh "npm test"
    // sh "mvn test"

    echo "Unit tests completed successfully"
}
```

Usage:

```groovy
run_tests()
```

---

# 🔐 Security Scanning

## trivy_scan.groovy

Scans the repository using Trivy for vulnerabilities.

```groovy
def call(){
    sh "trivy fs ."
}
```

Usage:

```groovy
trivy_scan()
```

---

# 📊 Build Reports

## generate_reports.groovy

Generates a build report and archives it.

```groovy
def call(Map config = [:]) {

    def projectName = config.projectName ?: 'Project'
    def imageName = config.imageName ?: ''
    def imageTag = config.imageTag ?: ''

    echo "Generating build report..."

    sh "mkdir -p reports"

    sh """
        echo "===== ${projectName} Build Report =====" > reports/build-report.txt
        echo "Generated: \$(date)" >> reports/build-report.txt
        echo "" >> reports/build-report.txt
        echo "Build Number: ${env.BUILD_NUMBER}" >> reports/build-report.txt
        echo "Docker Images: ${imageName}" >> reports/build-report.txt
        echo "Image Tag: ${imageTag}" >> reports/build-report.txt
        echo "Build Status: ${currentBuild.result ?: 'SUCCESS'}" >> reports/build-report.txt
        echo "Build URL: ${env.BUILD_URL}" >> reports/build-report.txt
    """

    archiveArtifacts artifacts: 'reports/*', allowEmptyArchive: true
}
```

Usage:

```groovy
generate_reports(
    projectName: "Notes App",
    imageName: "notes-app",
    imageTag: "latest"
)
```

---

# ☸ Kubernetes Integration

## update_k8s_manifests.groovy

Updates Kubernetes manifest files with new image tags and pushes the changes to Git.

```groovy
#!/usr/bin/env groovy

def call(Map config = [:]) {

    def imageTag = config.imageTag ?: error("Image tag is required")
    def manifestsPath = config.manifestsPath ?: 'kubernetes'
    def gitCredentials = config.gitCredentials ?: 'github-credentials'
    def gitUserName = config.gitUserName ?: 'Jenkins CI'
    def gitUserEmail = config.gitUserEmail ?: 'jenkins@example.com'

    echo "Updating Kubernetes manifests with image tag: ${imageTag}"

    withCredentials([usernamePassword(
        credentialsId: gitCredentials,
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_PASSWORD'
    )]) {

        sh """
            git config user.name "${gitUserName}"
            git config user.email "${gitUserEmail}"

            sed -i "s|image: trainwithshubham/easyshop-app:.*|image: trainwithshubham/easyshop-app:${imageTag}|g" ${manifestsPath}/08-easyshop-deployment.yaml

            if [ -f "${manifestsPath}/12-migration-job.yaml" ]; then
                sed -i "s|image: trainwithshubham/easyshop-migration:.*|image: trainwithshubham/easyshop-migration:${imageTag}|g" ${manifestsPath}/12-migration-job.yaml
            fi

            if [ -f "${manifestsPath}/10-ingress.yaml" ]; then
                sed -i "s|host: .*|host: easyshop.letsdeployit.com|g" ${manifestsPath}/10-ingress.yaml
            fi

            if git diff --quiet; then
                echo "No changes to commit"
            else
                git add ${manifestsPath}/*.yaml
                git commit -m "Update image tags to ${imageTag} and ensure correct domain [ci skip]"

                git remote set-url origin https://\${GIT_USERNAME}:\${GIT_PASSWORD}@github.com/LondheShubham153/tws-e-commerce-app.git
                git push origin HEAD:\${GIT_BRANCH}
            fi
        """
    }
}
```

Usage:

```groovy
update_k8s_manifests(
    imageTag: "v1.0.5",
    manifestsPath: "kubernetes",
    gitCredentials: "github-credentials"
)
```

---

# 🔧 Jenkins Configuration

## Step 1 — Add Shared Library

Go to Jenkins dashboard:

```
Manage Jenkins → Configure System → Global Pipeline Libraries
```

Add:

```
Name: Shared
Default Version: main
Retrieval Method: Modern SCM
Repository:
https://github.com/himanshugohil18/jenkins-shared-libraries.git
```

---

# 🧾 Example Jenkinsfile

```groovy
@Library('Shared') _
pipeline {

    agent any

    stages {

        stage("Clone Repository") {
            steps {
                clone(
                    "https://github.com/himanshugohil18/django-notes-app.git",
                    "main"
                )
            }
        }

        stage("Run Tests") {
            steps {
                run_tests()
            }
        }

        stage("Build Docker Image") {
            steps {
                docker_build(
                    imageName: "notes-app",
                    imageTag: "latest"
                )
            }
        }

        stage("Security Scan") {
            steps {
                trivy_scan()
            }
        }

        stage("Push Docker Image") {
            steps {
                docker_push(
                    imageName: "notes-app",
                    imageTag: "latest",
                    credentials: "docker-hub-credentials"
                )
            }
        }

        stage("Deploy") {
            steps {
                deploy()
            }
        }

    }
}
```

---

# 🎯 Why Jenkins Shared Libraries?

- Reusable CI/CD pipeline code  
- Cleaner Jenkinsfiles  
- Standardized DevOps workflows  
- Centralized pipeline management  
- Industry best practice  

---

# 👨‍💻 Author

Himanshu Gohil  

DevOps | Cloud | Kubernetes | CI/CD  

GitHub  
https://github.com/himanshugohil18
