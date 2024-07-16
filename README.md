# demo-ansattporten-integration
This is a simple application that demonstrates an how an verifier would be able to request and recieve the credential of a user, using the MATTR API.

The application creates a Request Template based on data required to request a credential issued by our [Issuer](https://github.com/felleslosninger/dc24-eu-wallet)
  
## To run application
Update the .env.example file with secrets specified there, and then rename it to .env

The MATTR secrets can be obtained by requesting access to their API and following their guide for setting up a verifier.

## Web stack alternatives
The application on main branch uses WebFlux and Netty, creating a reactive stack, through spring-boot-starter-webflux.  
The application in the "tomcat" branch uses a Servlet stack and tomcat, through spring-boot-starter-web
To read a little more about the differences: [spring-web vs spring-webflux](https://medium.com/@burakkocakeu/spring-web-vs-spring-webflux-9224260c47b5)
