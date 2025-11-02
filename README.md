Description:
This PR adds the initial backend components for user registration. It includes:

User entity with username, email, password, and security question/answer fields
UserRepository extending JpaRepository
UserService with password encryption using PasswordEncoder
RegistrationController with GET and POST endpoints for /register
Basic Spring Security configuration to allow access to the registration page
Initial Thymeleaf template for the registration form
Database configuration updates in application.properties
Notes:

Frontend design and form validation still need to be completed
Guest mode and admin user functionality to be implemented later
Testing:

Manual form submissions verified
User data successfully saved to database with encrypted passwords
