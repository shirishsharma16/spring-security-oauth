## Spring Security JWT with OAuth

### Relevant information:

1. `jwt-auth-server` is a Keycloak Authorization Server wrapped as a Spring Boot application
2. There is one OAuth Client registered in the Authorization Server:
   1. Client Id: jwtClient
   2. Client secret: jwtClientSecret
   3. Redirect Uri: http://localhost:8082/jwt-client/login/oauth2/code/custom
3. There are two users registered in the Authorization Server:
   1. john@test.com / 123
   2. mike@other.com / pass
    