package dev.creoii.greatbigworld.adventures.item;

import dev.creoii.greatbigworld.adventures.Adventures;
import dev.creoii.greatbigworld.adventures.component.JournalContentComponent;
import dev.creoii.greatbigworld.adventures.item.journal.JournalEntry;
import dev.creoii.greatbigworld.adventures.item.journal.JournalEntryHolder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;

public class JournalItem extends WrittenBookItem {
    public JournalItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.journal.title");
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        if (state.getBlock() instanceof JournalEntryHolder holder && context.getPlayer() != null) {
            if (!context.getWorld().isClient) {
                logEntry(context.getStack(), holder.creo$getJournalEntry());
            }
            return ActionResult.success(context.getWorld().isClient);
        }
        return super.useOnBlock(context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof JournalEntryHolder holder) {
            if (!user.getWorld().isClient) {
                logEntry(stack, holder.creo$getJournalEntry());
            }
            return ActionResult.success(user.getWorld().isClient);
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            StructureAccessor structureManager = serverWorld.getStructureAccessor();
            for (Structure entry : structureManager.getStructureReferences(user.getBlockPos()).keySet()) {
                if (structureManager.getStructureAt(user.getBlockPos(), entry).hasChildren()) {
                    logEntry(user.getStackInHand(hand), new JournalEntry.StructureEntry(entry));
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
        return super.use(world, user, hand);
    }

    public static void logEntry(ItemStack stack, JournalEntry journalEntry) {
        JournalContentComponent component = stack.get(Adventures.JOURNAL_CONTENT);
        if (component != null) {
            component.addEntry(journalEntry);
            List<JournalContentComponent.Page> pages = component.pages();
            JournalContentComponent component1 = new JournalContentComponent(pages);
            stack.set(Adventures.JOURNAL_CONTENT, component1);
        }
    }
}
