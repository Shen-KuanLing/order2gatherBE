# order2gatherBE
order2gather backend code

### Build and Install

    ./mvnw clean install

### Run

    ./mvnw spring-boot:run

### Pre-commit and Pre-push hooks
This project utilizes [pre-commit](https://pre-commit.com) to maintain code quality:

- **Pre-commit**: Automatically formats code to ensure consistent style using tools like [spotless](https://github.com/diffplug/spotless).

    - **How to use:**
        ```$ pre-commit install```
        ```$ pre-commit run --all-files```
    - for more details, please check [pre-commit](https://pre-commit.com/)
- **Pre-push**: Runs tests to ensure code integrity before pushing changes. Ensure that your changes pass all tests before pushing.
### Client Connect
- browser: local host
    http://localhost:8080/
- command line:
    <code>curl localhost:8080</code>
