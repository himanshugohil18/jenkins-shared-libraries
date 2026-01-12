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
clone(
    repoUrl: 'https://github.com/username/repo.git',
    branch: 'main'
)
```

---

### docker_build.groovy
Builds a Docker image.

Usage:
```groovy
docker_build(
    imageName: 'my-app',
    tag: 'latest'
)
```

---

### docker_push.groovy
Pushes Docker image to Docker Hub or any registry.

Prerequisite: Docker credentials must be configured in Jenkins.

Usage:
```groovy
docker_push(
    imageName: 'my-app',
    tag: 'latest',
    dockerCredId: 'dockerhub-creds'
)
```

---

### deploy.groovy
Deploys the application (extendable for Docker, EC2, Kubernetes).

Usage:
```groovy
deploy(
    appName: 'my-app',
    environment: 'production'
)
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
    agent any

    stages {

        stage('Hello') {
            steps {
                hello()
            }
        }

        stage('Clone') {
            steps {
                clone(
                    repoUrl: 'https://github.com/username/repo.git',
                    branch: 'main'
                )
            }
        }

        stage('Docker Build') {
            steps {
                docker_build(
                    imageName: 'my-app',
                    tag: 'latest'
                )
            }
        }

        stage('Docker Push') {
            steps {
                docker_push(
                    imageName: 'my-app',
                    tag: 'latest',
                    dockerCredId: 'dockerhub-creds'
                )
            }
        }

        stage('Deploy') {
            steps {
                deploy(
                    appName: 'my-app',
                    environment: 'production'
                )
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
