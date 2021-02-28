## Docker

```
sudo docker build -t rescue-app-nginx .
sudo docker run --init --rm -d -p 80:80 rescue-app-nginx
```

### Kill Docker process

```
docker kill <container_id>
```
