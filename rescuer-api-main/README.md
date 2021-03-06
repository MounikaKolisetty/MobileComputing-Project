### Build Rescuer App API

```
sudo docker build -t rescuer-app-service .
docker run -e "SPRING_APPLICATION_NAME=rescuer-app-service" -e "server.port=8080" -p 8080:8080 --init --rm -d --name rescuer-service rescuer-app-service
```
