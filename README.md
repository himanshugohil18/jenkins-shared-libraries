# Jenkins Shared Libraries 🚀

This repository contains reusable Jenkins Shared Library functions written in Groovy.  
These libraries help standardize CI/CD pipelines and reduce duplication across Jenkinsfiles.

---

## 📁 Repository Structure

```
jenkins-shared-libraries/
├── vars/
│   ├── clone.groovy
│   ├── docker_build.groovy
│   ├── docker_push.groovy
│   ├── deploy.groovy
│   └── hello.groovy
└── README.md
```

---

## 📌 Shared Library Functions

### hello.groovy
Prints a greeting message to verify library integration.

Usage:
```groovy
hello()
```

---

### clone.groovy
Clones a Git repository.

Usage:
```groovy
def call(String url, String branch){
  git url: "${url}", branch: "${branch}"
}
```

---

### docker_build.groovy
Builds a Docker image.

Usage:
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

---

### docker_push.groovy
Pushes Docker image to Docker Hub or any registry.

Prerequisite: Docker credentials must be configured in Jenkins.

Usage:
```groovy
def call(Map config = [:]) {

    def imageName = config.imageName ?: error("Image name is required")
    def imageTag  = config.imageTag ?: 'latest'
    def credentials = config.credentials ?: 'dockerHubCred'  // ✅ FIXED

    echo "Pushing Docker image: ${imageName}:${imageTag}"

    withCredentials([
        usernamePassword(
            credentialsId: credentials,
            usernameVariable: 'DOCKER_USERNAME',
            passwordVariable: 'DOCKER_PASSWORD'
        )
    ]) {
        sh """
            echo "\$DOCKER_PASSWORD" | docker login -u "\$DOCKER_USERNAME" --password-stdin
            docker tag ${imageName}:${imageTag} \$DOCKER_USERNAME/${imageName}:${imageTag}
            docker push \$DOCKER_USERNAME/${imageName}:${imageTag}
        """
    }
}
```

---

### deploy.groovy
Deploys the application (extendable for Docker, EC2, Kubernetes).

Usage:
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

---

## 🔧 Jenkins Configuration

### Step 1: Add Shared Library

Go to:
Manage Jenkins → Configure System → Global Pipeline Libraries

Add:

```
Name: Shared
Default Version: main
Repository URL: https://github.com/himanshugohil18/jenkins-shared-libraries.git
```

---

### Step 2: Jenkinsfile Example

```groovy
@Library('Shared') _
pipeline {
    agent { label 'vinod' }

    stages {

        stage("Code Clone") {
            steps {
                sh "whoami"
                clone(
                    "https://github.com/himanshugohil18/django-notes-app.git",
                    "main"
                )
            }
        }

        stage("Code Build") {
            steps {
                docker_build(
                    imageName: "notes-app",
                    tag: "latest"
                )
            }
        }

        stage("Push to DockerHub") {
            steps {
                docker_push(
                    imageName: "notes-app",
                    imageTag: "latest",
                    credentials: "dockerHubCred"
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

## 🧠 Why Jenkins Shared Libraries?

- Reusable CI/CD logic
- Clean Jenkinsfiles
- Centralized pipeline management
- Scalable DevOps workflows
- Industry best practice

---

## 🚀 Future Enhancements

- Kubernetes deployments
- Helm charts
- Terraform integration
- Slack notifications
- Security scanning (Trivy, SonarQube)

---

## 👨‍💻 Author

Himanshu Gohil  
DevOps | CI/CD | Cloud | Automation  

GitHub: https://github.com/himanshugohil18
