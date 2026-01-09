def call() {
    echo "Starting deployment..."

    sh """
        docker stop notes-app || true
        docker rm notes-app || true

        docker run -d \
          --name notes-app \
          -p 8000:8000 \
          himanshugohil18/notes-app:latest
    """

    echo "Deployment completed successfully"
}
