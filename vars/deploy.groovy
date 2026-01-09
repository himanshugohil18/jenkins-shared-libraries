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
