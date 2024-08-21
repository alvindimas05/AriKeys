package org.alvindimas05.neoforge;

import net.neoforged.fml.common.Mod;

import org.alvindimas05.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
