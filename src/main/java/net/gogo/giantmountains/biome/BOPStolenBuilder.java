package net.gogo.giantmountains.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

public class BOPStolenBuilder {
    protected final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    protected final Climate.Parameter[] temperatures = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.45F), // Cold
            Climate.Parameter.span(-0.45F, -0.15F), // Cool
            Climate.Parameter.span(-0.15F, 0.2F),   // Neutral
            Climate.Parameter.span(0.2F, 0.55F),   // Warm
            Climate.Parameter.span(0.55F, 1.0F)    // Hot
    };

    protected final Climate.Parameter[] humidities = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.35F), // Dry
            Climate.Parameter.span(-0.35F, -0.1F),  // Semi-dry
            Climate.Parameter.span(-0.1F, 0.1F),    // Neutral
            Climate.Parameter.span(0.1F, 0.3F),    // Semi-wet
            Climate.Parameter.span(0.3F, 1.0F)     // Wet
    };

    protected final Climate.Parameter[] erosions = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.78F), // High erosion (peaks)
            Climate.Parameter.span(-0.78F, -0.375F), // Mid erosion (mid-mountain)
            Climate.Parameter.span(-0.375F, -0.2225F), // Low erosion (low-mountain)
            Climate.Parameter.span(-0.2225F, 0.05F), // Valleys
            Climate.Parameter.span(0.05F, 0.45F),   // River edges
            Climate.Parameter.span(0.45F, 0.55F),   // Rivers
            Climate.Parameter.span(0.55F, 1.0F)    // Flat areas (unused in this region)
    };

    // Continentalness ranges for mountain regions
    protected final Climate.Parameter mountainContinentalness = Climate.Parameter.span(0.3F, 1.0F); // Far inland (mountains)
    protected final Climate.Parameter valleyContinentalness = Climate.Parameter.span(-0.11F, 0.3F); // Valleys and rivers

    // Custom mountain biomes
    protected final ResourceKey<Biome> PEAK_BIOME = Biomes.PEAK_MOUNTAIN; // Example peak biome
    protected final ResourceKey<Biome> MID_MOUNTAIN_BIOME = Biomes.MID_MOUNTAIN; // Example mid-mountain biome
    protected final ResourceKey<Biome> LOW_MOUNTAIN_BIOME = Biomes.LOW_MOUNTAIN; // Example low-mountain biome
    protected final ResourceKey<Biome> VALLEY_BIOME = Biomes.EDGE_MOUNTAIN; // Example valley biome
    protected final ResourceKey<Biome> RIVER_BIOME = net.minecraft.world.level.biome.Biomes.RIVER; // Vanilla river biome

    /******************************************************************************************************************************/

    public void addBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper)
    {
        this.addMountainBiomes(biomeRegistry, mapper);
        this.addValleyAndRiverBiomes(biomeRegistry, mapper);
    }

    private void addMountainBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper)
    {
        for (int i = 0; i < this.temperatures.length; ++i)
        {
            Climate.Parameter temperature = this.temperatures[i];

            for (int j = 0; j < this.humidities.length; ++j)
            {
                Climate.Parameter humidity = this.humidities[j];

                // Peaks (high erosion)
                this.addSurfaceBiome(mapper, temperature, humidity, mountainContinentalness, erosions[0], FULL_RANGE, 0.0F, PEAK_BIOME);

                // Mid-mountain (mid erosion)
                this.addSurfaceBiome(mapper, temperature, humidity, mountainContinentalness, erosions[1], FULL_RANGE, 0.0F, MID_MOUNTAIN_BIOME);

                // Low-mountain (low erosion)
                this.addSurfaceBiome(mapper, temperature, humidity, mountainContinentalness, erosions[2], FULL_RANGE, 0.0F, LOW_MOUNTAIN_BIOME);
            }
        }
    }

    private void addValleyAndRiverBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper)
    {
        for (int i = 0; i < this.temperatures.length; ++i)
        {
            Climate.Parameter temperature = this.temperatures[i];

            for (int j = 0; j < this.humidities.length; ++j)
            {
                Climate.Parameter humidity = this.humidities[j];

                // Valleys
                this.addSurfaceBiome(mapper, temperature, humidity, valleyContinentalness, erosions[3], FULL_RANGE, 0.0F, VALLEY_BIOME);

                // Rivers
                this.addSurfaceBiome(mapper, temperature, humidity, valleyContinentalness, erosions[5], FULL_RANGE, 0.0F, RIVER_BIOME);
            }
        }
    }

    /******************************************************************************************************************************/

    protected void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome)
    {
        mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biome));
        mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biome));
    }
}
