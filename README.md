# 🚀 Jenkins Shared Libraries

This repository contains reusable Jenkins Shared Library functions written in Groovy.

These libraries help standardize CI/CD pipelines, reduce duplicated code, and simplify Jenkins pipeline development across multiple projects.

The shared library provides reusable steps for:

- Git repository cloning
- Docker image build & push
- Running tests
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

Used to test whether the shared library is successfully loaded.

Example:

```groovy
hello()
```

---

# 📦 CI/CD Pipeline Functions

## clone.groovy

Clones a Git repository.

Example:

```groovy
clone(
 "https://github.com/your-repo/project.git",
 "main"
)
```

---

## docker_build.groovy

Builds a Docker image.

Example:

```groovy
docker_build(
    imageName: "notes-app",
    imageTag: "latest"
)
```

---

## docker_push.groovy

Pushes the built Docker image to a container registry.

Example:

```groovy
docker_push(
    imageName: "notes-app",
    imageTag: "latest",
    credentials: "dockerHubCred"
)
```

---

# 🧪 Testing & Quality

## run_tests.groovy

Runs project test cases before building the application.

Example:

```groovy
run_tests()
```

---

## generate_reports.groovy

Generates build or test reports.

Example:

```groovy
generate_reports()
```

---

# 🔐 Security Scanning

## trivy_scan.groovy

Runs vulnerability scanning using Trivy.

Example:

```groovy
trivy_scan(
  imageName: "notes-app",
  imageTag: "latest"
)
```

Detects:

- OS vulnerabilities
- Library vulnerabilities
- Misconfigurations

---

# ☸ Kubernetes Integration

## update_k8s_manifests.groovy

Updates the image tag in Kubernetes manifest files.

Example:

```groovy
update_k8s_manifests(
  imageName: "notes-app",
  imageTag: "latest"
)
```

---

# 🚀 Deployment

## deploy.groovy

Deploys the application container.

Example:

```groovy
deploy()
```

Deployment flow:

1. Stop existing container
2. Remove old container
3. Run new container with updated image

---

# 🔧 Jenkins Configuration

## Step 1: Add Shared Library

Go to:

```
Manage Jenkins → Configure System → Global Pipeline Libraries
```

Add the library:

```
Name: Shared
Default Version: main
Retrieval Method: Modern SCM
Repository URL:
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
                trivy_scan(
                    imageName: "notes-app",
                    imageTag: "latest"
                )
            }
        }

        stage("Push Image") {
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

# 🎯 Why Jenkins Shared Libraries?

- Reusable CI/CD pipeline code
- Cleaner Jenkinsfiles
- Centralized pipeline management
- Scalable DevOps workflows
- Industry best practice

---

# 🚀 Future Enhancements

- Helm deployment support
- SonarQube integration
- Slack notifications
- Terraform automation
- GitOps workflow with ArgoCD

---

# 👨‍💻 Author

Himanshu Gohil  

DevOps | Cloud | Kubernetes | CI/CD  

GitHub  
https://github.com/himanshugohil18
