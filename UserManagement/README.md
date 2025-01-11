### DESCRIPTION

    This is the api that handles app's users.

#### API

    Register a new user
    Authenticate a user 
    Add user
    Delete user
    Get one user by id or get all users

### END-POINTS

    POST /auth/register
        - Only user which can register is the user with roles: CLIENT or ADMIN
        - Registers a new user
        - Returns a token
        - Response: 200 OK

    POST /auth/login
        - Any user can login
        - Authenticates a user
        - Returns a token
        - Response 200 OK

    POST /user
        - Only user with role ADMIN can access this endpoint /**/
        - Create a new user
        - Returns the user added
        - Response 201 CREATED

    GET /user/{id}
        - Only user with role ADMIN can access this endpoint /**/
        - Returns an user by id
        - Response 200 OK

    GET /user
        - Only user with role ADMIN can access this endpoint /**/
        - Returns user list
        - Response 200 OK

    DELETE /user/{id}
        - Only user with role ADMIN can access this endpoint /**/
        - Deletes an user by id
        - Response: 204 NO CONTENT

### CONSUMES, PRODUCES

**Consumes:**

    - application/json

**Produces:**

    - application/json

### SWAGGER

[swagger-ui](http://172.17.0.2:8080/swagger-ui/index.html#/)

### RUN LOCALLY

  /**/  Create database on terminal, running the command: mvn flyway:migrate to create tables.

