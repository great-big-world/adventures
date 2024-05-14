package dev.creoii.greatbigworld.adventures.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.adventures.item.journal.JournalEntry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record JournalContentComponent(List<Page> pages) {
    private static final int MAX_PAGE_SIZE = 10;
    public static final Codec<JournalContentComponent> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.list(Page.CODEC).fieldOf("pages").forGetter(JournalContentComponent::pages)).apply(instance, JournalContentComponent::new);
    });
    public static final PacketCodec<PacketByteBuf, JournalContentComponent> PACKET_CODEC = Page.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(JournalContentComponent::new, JournalContentComponent::pages);

    public List<Text> getPages() {
        return Lists.transform(pages, input -> Text.literal(input.text));
    }

    public void addEntry(JournalEntry entry) {
        if (containsEntry(entry))
            return;

        for (Page page : pages) {
            if (page.canAddEntry(entry)) {
                page.addEntry(entry);
                return;
            }
        }

        addPage(entry);
    }

    private void addPage(JournalEntry entry) {
        pages.add(new Page(entry.getData(), entry.getSize()));
    }

    public boolean containsEntry(JournalEntry entry) {
        for (Page page : pages) {
            for (String s : page.text.split("\n")) {
                if (entry.getData().equals(s))
                    return true;
            }
        }
        return false;
    }


    public static class Page {
        public static final Codec<Page> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(Codec.STRING.fieldOf("text").forGetter(Page::getText), Codecs.POSITIVE_INT.fieldOf("size").forGetter(Page::getSize)).apply(instance, Page::new);
        });
        public static final PacketCodec<PacketByteBuf, Page> PACKET_CODEC = new PacketCodec<>() {
            @Override
            public Page decode(PacketByteBuf buf) {
                return new Page(buf.readString(), buf.readInt());
            }

            @Override
            public void encode(PacketByteBuf buf, Page value) {
                buf.writeString(value.text);
                buf.writeInt(value.size);
            }
        };
        private String text;
        private int size;

        public Page(String startText, int startSize) {
            text = startText;
            size = startSize;
        }

        public void addEntry(JournalEntry entry) {
            text = text.concat("\n").concat(entry.getData());
            size += entry.getSize();
        }

        public String getText() {
            return text;
        }

        public int getSize() {
            return size;
        }

        public boolean canAddEntry(JournalEntry entry) {
            return entry.getSize() + getSize() <= MAX_PAGE_SIZE;
        }

        public static Page read(PacketByteBuf buf) {
            return new Page(buf.readString(), buf.readInt());
        }

        public static void write(PacketByteBuf buf, Page page) {
            buf.writeString(page.text);
            buf.writeInt(page.size);
        }
    }
}
