### Docker instructions

Create a credentials file (in this folder), assigned to the default profile. Should look like this:
```
[default]
aws_access_key_id = SOMEKEYTHATSHOULDBEFILLED
aws_secret_access_key = somesecrethatshouldbefilled
```

(Re)build the image, start the container, detached from the tty and check the logs
```bash
docker-compose down
docker-compose up -d --build; docker-compose logs -f
```
### Configuration

#### Kinesis stream
Modify KINESIS_STREAM env var in docker-compose.yaml

#### Credentials
Modify credentials file. it's in .gitignore, so it won't (shouldn't) be committed

#### TODO
Externalize properties from "Client/Gatling-Kinesis-master/src/test/scala/cache/client/Device.scala"
