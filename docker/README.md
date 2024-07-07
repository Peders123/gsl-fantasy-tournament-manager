## Building the Projects

From within this directory, simply run:

**IMPORTANT:** Before building, update all the values in the 'environment.sh' file to export the correct env vars.

A config file will need to be created in the root directory as well for the bot to run. Dm @peders on discord to find out about this as the documentation isn't there yet.

```
docker-compose build <janus|terra|mercury>
docker-compose up -d <janus|terra|mercury>
```

The ports maybe need some configuration on terra/mercury to run on azure, but it shouldn't be too much to change.