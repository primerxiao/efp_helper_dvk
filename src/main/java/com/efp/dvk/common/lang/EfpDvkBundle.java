package com.efp.dvk.common.lang;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class EfpDvkBundle extends DynamicBundle {
    public static final @NonNls String BUNDLE = "messages.BasicBundle";
    public static final EfpDvkBundle INSTANCE = new EfpDvkBundle();

    public EfpDvkBundle() {
        super(BUNDLE);
    }

    public static @NotNull @Nls String message(@NotNull @PropertyKey(
            resourceBundle = BUNDLE
    ) String key, @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }
}
