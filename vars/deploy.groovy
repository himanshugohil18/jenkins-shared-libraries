def call() {
    echo "Starting deployment..."

    sh """
        echo "Stopping any container using port 8000..."
        docker ps -q --filter "publish=8000" | xargs -r docker stop
        docker ps -aq --filter "publish=8000" | xargs -r docker rm

        echo "Running new container..."
        docker run -d \
          --name notes-app \
          -p 8000:8000 \
          himanshugohil18/notes-app:latest
    """

    echo "Deployment completed successfully"
}
