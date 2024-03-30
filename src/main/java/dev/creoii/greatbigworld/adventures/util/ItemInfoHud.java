package dev.creoii.greatbigworld.adventures.util;

public class ItemInfoHud {
    private boolean active = false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void invert() {
        active = !active;
    }
}
