package dev.creoii.greatbigworld.adventures.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class LeashKnotRenderState extends EntityRenderState {
    private boolean thin;

    public void setThin(boolean thin) {
        this.thin = thin;
    }

    public boolean isThin() {
        return thin;
    }
}
