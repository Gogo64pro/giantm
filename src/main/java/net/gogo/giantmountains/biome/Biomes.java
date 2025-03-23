package net.gogo.giantmountains.biome;

import net.gogo.giantmountains.Giantmountains;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class Biomes {
    public static final ResourceKey<Biome> PEAK_MOUNTAIN = register("peak_mountain");
    public static final ResourceKey<Biome> MID_MOUNTAIN = register("mid_mountain");
    public static final ResourceKey<Biome> LOW_MOUNTAIN = register("low_mountain");
    public static final ResourceKey<Biome> EDGE_MOUNTAIN = register("edge_mountain");

    private static ResourceKey<Biome> register(String name)
    {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(Giantmountains.MODID, name));
    }
}
