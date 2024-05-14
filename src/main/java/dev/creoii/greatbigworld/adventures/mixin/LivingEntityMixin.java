package dev.creoii.greatbigworld.adventures.mixin;

import dev.creoii.greatbigworld.adventures.item.journal.JournalEntry;
import dev.creoii.greatbigworld.adventures.item.journal.JournalEntryHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements JournalEntryHolder {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public JournalEntry creo$getJournalEntry() {
        return new JournalEntry.EntityEntry((LivingEntity) (Object) this);
    }
}
