package net.seyarada.pandeloot.flags;

import net.seyarada.pandeloot.flags.enums.FlagPriority;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value= RetentionPolicy.RUNTIME)
public @interface FlagEffect {
public String id() default "";

public String[] description() default {};

public FlagPriority priority() default FlagPriority.NORMAL;

//public FlagScope[] scope() default FlagScope.AUTO;

}

