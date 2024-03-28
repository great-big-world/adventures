package dev.creoii.greatbigworld.adventures.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.function.Consumer;

public abstract class OptionSliderWidget<T> extends SliderWidget {
    public T tValue;
    private final boolean clamp;
    private final Consumer<T> valueApplier;
    private final T[] values;

    @SafeVarargs
    public OptionSliderWidget(int x, int y, int width, int height, T value, boolean clamp, Consumer<T> valueApplier, T... values) {
        super(x, y, width, height, Text.empty(), 0d);
        tValue = value;
        this.value = findPercentValue(values, value);
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

    private static <T> T findClosestValue(T[] array, double target) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        if (target < 0 || target > 1) {
            throw new IllegalArgumentException("Target value must be between 0 and 1");
        }

        int index = Math.round((float) target * (array.length - 1));
        return array[index];
    }

    private static <T> float findPercentValue(T[] array, T target) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        if (!Arrays.asList(array).contains(target)) {
            throw new IllegalArgumentException("Target value not found in the array");
        }

        int index = Arrays.asList(array).indexOf(target);
        return (float) index / (array.length - 1);
    }
}
