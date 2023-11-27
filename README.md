# order2gatherBE
order2gather backend code

### Build and Install

    ./mvnw clean install

### Run

    ./mvnw spring-boot:run

### Pre-commit and Pre-push hooks
This project utilizes [git build hooks](https://github.com/rudikershaw/git-build-hook) to maintain code quality:

- **Pre-commit**: Automatically formats code to ensure consistent style using tools like [spotless](https://github.com/diffplug/spotless).

- **Pre-push**: Runs tests to ensure code integrity before pushing changes. Ensure that your changes pass all tests before pushing.

### Client Connect
- browser: local host  
    http://localhost:8080/
- command line:
    <code>curl localhost:8080</code>