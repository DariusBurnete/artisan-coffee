The images folder path needs to be changed in the following files: Webconfig, ImageService, ProductService.  
For admin creation, in SecurityConfig move '"/h2-console/**"' from first '.requestMatchers' to second '.requestMatchers'.  
Then access http://localhost:8080/h2-console, click "Connect", select users table, create admin, move '"/h2-console/**"' back.  
  
In application.properties the line: spring.mail.username= needs a valid, existent gmail and spring.mail.password= needs a generated app password by your google account with same email.  
  
For kafka:  
Run Docker Desktop  
Run docker compose up -d (only the first time) in compiler terminal, the rest of the times you start the containers from Docker Desktop.
