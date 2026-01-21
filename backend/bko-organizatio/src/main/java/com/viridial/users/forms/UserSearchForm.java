package com.viridial.users.forms;

import com.viridial.common.form.SearchForm;

/**
 * Search form for UserEntity.
 */
public class UserSearchForm extends SearchForm {
    private String email;
    private String firstName;
    private String lastName;
    private Boolean active;
    private Long statusId;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}

