package dev.creoii.greatbigworld.adventures.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public abstract class OptionSliderWidget<T> extends SliderWidget {
    public T tValue;
    private final boolean clamp;
    private final Consumer<T> valueApplier;
    private final List<T> values;

    public OptionSliderWidget(int x, int y, int width, int height, T value, boolean clamp, Consumer<T> valueApplier, List<T> values) {
        super(x, y, width, height, Text.empty(), findPercentValue(values, value));
        tValue = value;
        this.clamp = clamp;
        this.valueApplier = valueApplier;
        this.values = values;
        updateMessage();
    }

    @Override
    protected void applyValue() {
        tValue = findClosestValue(values, value);
        if (clamp)
            value = findPercentValue(values, tValue);
        valueApplier.accept(tValue);
    }

    private static <T> T findClosestValue(List<T> list, double index) {
        return list.get(Math.round((float) index * (list.size() - 1)));
    }

    private static <T> float findPercentValue(List<T> list, T target) {
        return (float) list.indexOf(target) / (list.size() - 1);
    }
}
