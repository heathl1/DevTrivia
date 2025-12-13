package com.example.DevTrivia.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 30, message = "Username must be 3 to 30 characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 72, message = "Password must be 8 to 72 characters.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,72}$",
            message = "Password must include an uppercase letter, a lowercase letter, a number, and a special character."
    )
    private String password;

    @NotBlank(message = "Please confirm your password.")
    private String confirmPassword;

    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Security question is required.")
    private String securityQuestion;

    @NotBlank(message = "Security answer is required.")
    @Size(min = 3, max = 100, message = "Security answer must be 3 to 100 characters.")
    private String securityAnswer;

    // This creates a form-level validation error if passwords do not match
    @AssertTrue(message = "Passwords do not match.")
    public boolean isPasswordsMatch() {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }

    // Getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }
}