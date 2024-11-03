package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookScreen.class)
public abstract class BookScreenMixin extends Screen {
    @Shadow private BookScreen.Contents contents;
    @Unique private static final Identifier PIG_ENTRY_TEXTURE = Identifier.of(GreatBigWorld.NAMESPACE, "textures/gui/journal/entity/pig.png");

    protected BookScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), cancellable = true)
    private void gbw$renderJournalEntries(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        contents.pages().forEach(text -> {
            if (text.getString().equals(EntityType.PIG.getTranslationKey())) {
                context.drawTexture(PIG_ENTRY_TEXTURE, (width - 120) / 2, 32, 0, 0, 63, 56, 63, 56);
            }
        });
        ci.cancel();
    }
}
