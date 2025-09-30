package org.factoriaf5.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileTest {

  @Test
  void shouldCreateProfileWithBuilder() {
    UserProfile profile = UserProfile.builder()
        .id(1L)
        .firstName("Ana")
        .lastName("Martinez")
        .email("ana@example.com")
        .phoneNumber("600123456")
        .build();

    assertThat(profile.getId()).isEqualTo(1L);
    assertThat(profile.getFirstName()).isEqualTo("Ana");
    assertThat(profile.getLastName()).isEqualTo("Martinez");
    assertThat(profile.getEmail()).isEqualTo("ana@example.com");
    assertThat(profile.getPhoneNumber()).isEqualTo("600123456");
  }

  @Test
  void shouldAllowSettingAndGettingUser() {
    User user = User.builder().dni("12345678A").build();

    UserProfile profile = new UserProfile();
    profile.setUser(user);
    user.setProfile(profile);

    assertThat(profile.getUser()).isEqualTo(user);
    assertThat(user.getProfile()).isEqualTo(profile);
  }

  @Test
  void equalsShouldReturnTrueForProfilesWithSameData() {
    UserProfile profile1 = UserProfile.builder()
        .id(1L)
        .firstName("Ana")
        .lastName("Martinez")
        .email("ana@example.com")
        .phoneNumber("600123456")
        .build();

    UserProfile profile2 = UserProfile.builder()
        .id(1L)
        .firstName("Ana")
        .lastName("Martinez")
        .email("ana@example.com")
        .phoneNumber("600123456")
        .build();

    assertThat(profile1).isEqualTo(profile2);
    assertThat(profile1.hashCode()).isEqualTo(profile2.hashCode());
  }

  @Test
  void equalsShouldReturnFalseForProfilesWithDifferentData() {
    UserProfile profile1 = UserProfile.builder()
        .id(1L)
        .firstName("Ana")
        .lastName("Martinez")
        .email("ana@example.com")
        .phoneNumber("600123456")
        .build();

    UserProfile profile2 = UserProfile.builder()
        .id(2L)
        .firstName("Juan")
        .lastName("Lopez")
        .email("juan@example.com")
        .phoneNumber("600654321")
        .build();

    assertThat(profile1).isNotEqualTo(profile2);
  }

  @Test
  void toStringShouldContainFieldValues() {
    UserProfile profile = UserProfile.builder()
        .id(1L)
        .firstName("Ana")
        .lastName("Martinez")
        .email("ana@example.com")
        .phoneNumber("600123456")
        .build();

    String toString = profile.toString();

    assertThat(toString).contains("Ana");
    assertThat(toString).contains("Martinez");
    assertThat(toString).contains("ana@example.com");
  }
}
