# Sample project 

... for running test against a browser running in Docker.

## How to run tests

Before running this test, execute from command line:
> docker run -p 4444:4444 -p 7900:7900 --shm-size="2g" -e "SE_NODE_GRID_URL=http://localhost:4444" seleniarm/standalone-chromium

Then run the test:

> ./gradlew test


## Result
> BUILD SUCCESSFUL


## References

* https://github.com/SeleniumHQ/docker-selenium