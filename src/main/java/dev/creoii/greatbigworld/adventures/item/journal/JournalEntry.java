package dev.creoii.greatbigworld.adventures.item.journal;

import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public interface JournalEntry {
    int getSize();

    String getData();

    record EntityEntry(Entity entity) implements JournalEntry {
        @Override
        public int getSize() {
            return 5;
        }

        @Override
        public String getData() {
            return entity.getType().getTranslationKey();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof EntityEntry entry) {
                return entry.entity.getType() == entity.getType();
            }
            return false;
        }
    }

    record StructureEntry(Structure structure) implements JournalEntry {
        @Override
        public int getSize() {
            return 10;
        }

        @Override
        public String getData() {
            Identifier id = Registries.STRUCTURE_TYPE.getId(structure.getType());
            return id != null ? id.toString() : "";
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof StructureEntry entry) {
                return entry.structure.getType() == structure.getType();
            }
            return false;
        }
    }
}
