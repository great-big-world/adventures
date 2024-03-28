package dev.creoii.greatbigworld.adventures.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.function.Consumer;

public abstract class OptionSliderWidget<T> extends SliderWidget {
    private final Consumer<T> valueApplier;
    private final T[] values;
    public T tValue;

    @SafeVarargs
    public OptionSliderWidget(int x, int y, int width, int height, T value, Consumer<T> valueApplier, T... values) {
        super(x, y, width, height, Text.empty(), 0d);
        this.valueApplier = valueApplier;
        this.values = values;
        tValue = value;
        this.value = findPercentValue(values, value);
        updateMessage();
    }

    @Override
    protected void applyValue() {
        tValue = findClosestValue(values, value);
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
