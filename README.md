The images folder path needs to be changed in the following files: Webconfig, ImageService, ProductService.  
For admin creation, in SecurityConfig move '"/h2-console/**"' from first '.requestMatchers' to second '.requestMatchers'.  
In application.properties the line: spring.mail.username= needs a valid, existent email and spring.mail.password= needs a generated app password by your google account with same email.  
  
For kafka:  
Run Docker Desktop  
Run docker compose up -d (only the first time) in compiler terminal, the rest of the times you start the containers from Docker Desktop.
