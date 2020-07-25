# Spring Boot Angular Application Template

* [x] Backend with Jwt Authentication
* [x] Jwt refresh token implemented
* [x] Application properties 
* [x] Add Angular frontend 
* [x] Global notification service in angular 
* [ ] Link frontend with backend

## Backend architecture
The backend uses a layered architecture to provide maximum modularity.
* **_config:_** configuration for the application 
* **_domain:_** holds the entities of the application that will be created automaticly by hibernate
* **_endpoints:_** contains all the rest endpoints that can be called from the client
* **_exceptions:_** all the custom exceptions that can be thrown by the backend
* **_persistence:_** Spring data jpa repositories
* **_security:_** all the security configuration and jwt things ;)
* **_service:_** holding the business logic 

## Applicationproperties
For the backend you can define properties in the `application.yml` file. The class `ApplicationProperties` has fields that access the properties with the prefix `application` from the `application.yml` file. 

## Applicationroles
Roles can be defined in the enum `ApplicationRoles`. At application startup the roles will be created if they dont exist already in the db. 

## Notificationservice Frontend
To get notification global on the frontend there is a notificationservice.
Usage:
```Typescript
export class LoginComponent implements OnInit {

  constructor(private notificationService: NotificationService) {

    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });

  }
}
```  

Note: 
* https://github.com/Angular2Guy/AngularPortfolioMgr/blob/master/src/main/java/ch/xxx/manager/jwt/JwtTokenProvider.java
* https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
* https://dzone.com/articles/angular-jwt-token-autoupdates-with-spring-boot
