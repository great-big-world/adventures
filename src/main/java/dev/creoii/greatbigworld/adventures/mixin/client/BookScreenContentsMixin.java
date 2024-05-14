package dev.creoii.greatbigworld.adventures.mixin.client;

import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.component.JournalContentComponent;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookScreen.Contents.class)
public class BookScreenContentsMixin {
    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void gbw$createJournalContents(ItemStack stack, CallbackInfoReturnable<BookScreen.Contents> cir) {
        JournalContentComponent component = stack.get(Adventures.JOURNAL_CONTENT);
        if (component != null) {
            cir.setReturnValue(new BookScreen.Contents(component.getPages()));
        }
    }
}
