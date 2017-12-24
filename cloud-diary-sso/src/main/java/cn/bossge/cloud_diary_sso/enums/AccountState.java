package cn.bossge.cloud_diary_sso.enums;

public enum AccountState {
    ACTIVE,
    INACTIVE,
    PENDING_FOR_ACTIVE;

    public boolean isPendingForActive() {
        return this.equals(PENDING_FOR_ACTIVE);
    }
}
