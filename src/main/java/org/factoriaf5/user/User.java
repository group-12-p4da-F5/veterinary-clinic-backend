package org.factoriaf5.user;

import java.util.Set;

import org.factoriaf5.patient.Patient;
import org.factoriaf5.role.Role;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
  @Id
  @Column(name = "dni")
  private String dni;

  private String passwordHash;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  Set<Role> roles;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private UserProfile profile;

  @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Patient> patients;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof User))
      return false;
    return dni != null && dni.equals(((User) o).getDni());
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
