package kz.azamatbakyt.BarberTelegramBot.helpers;

public enum Roles {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;
    Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
