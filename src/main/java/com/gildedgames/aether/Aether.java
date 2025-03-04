package com.gildedgames.aether;

import com.gildedgames.aether.advancement.AetherAdvancementTriggers;
import com.gildedgames.aether.api.AetherMoaTypes;
import com.gildedgames.aether.block.dispenser.DispenseUsableItemBehavior;
import com.gildedgames.aether.blockentity.AetherBlockEntityTypes;
import com.gildedgames.aether.block.AetherBlocks;
import com.gildedgames.aether.block.AetherCauldronInteractions;
import com.gildedgames.aether.block.dispenser.AetherDispenseBehaviors;
import com.gildedgames.aether.client.particle.AetherParticleTypes;
import com.gildedgames.aether.client.AetherSoundEvents;
import com.gildedgames.aether.blockentity.IncubatorBlockEntity;
import com.gildedgames.aether.block.dispenser.DispenseDartBehavior;
import com.gildedgames.aether.blockentity.AltarBlockEntity;
import com.gildedgames.aether.blockentity.FreezerBlockEntity;
import com.gildedgames.aether.data.generators.*;
import com.gildedgames.aether.data.generators.tags.*;
import com.gildedgames.aether.effect.AetherEffects;
import com.gildedgames.aether.entity.AetherEntityTypes;
import com.gildedgames.aether.entity.ai.AetherBlockPathTypes;
import com.gildedgames.aether.entity.ai.brain.memory.AetherMemoryModuleTypes;
import com.gildedgames.aether.entity.ai.brain.sensing.AetherSensorTypes;
import com.gildedgames.aether.event.AetherGameEvents;
import com.gildedgames.aether.inventory.menu.AetherMenuTypes;
import com.gildedgames.aether.inventory.AetherRecipeBookTypes;
import com.gildedgames.aether.item.AetherItems;
import com.gildedgames.aether.loot.conditions.AetherLootConditions;
import com.gildedgames.aether.loot.functions.AetherLootFunctions;
import com.gildedgames.aether.loot.modifiers.AetherLootModifiers;
import com.gildedgames.aether.recipe.AetherRecipeSerializers;
import com.gildedgames.aether.recipe.AetherRecipeTypes;
import com.gildedgames.aether.world.AetherPoi;
import com.gildedgames.aether.world.foliageplacer.AetherFoliagePlacerTypes;
import com.gildedgames.aether.world.feature.AetherFeatures;
import com.gildedgames.aether.world.placementmodifier.AetherPlacementModifiers;
import com.gildedgames.aether.network.AetherPacketHandler;
import com.gildedgames.aether.client.CombinedResourcePack;
import com.gildedgames.aether.api.SunAltarWhitelist;
import com.gildedgames.aether.api.TriviaGenerator;
import com.gildedgames.aether.world.processor.AetherStructureProcessors;
import com.gildedgames.aether.world.structure.AetherStructureTypes;
import com.gildedgames.aether.world.structurepiece.AetherStructurePieceTypes;
import com.gildedgames.aether.world.treedecorator.AetherTreeDecoratorTypes;
import com.google.common.reflect.Reflection;
import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.resource.PathPackResources;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Aether.MODID)
@Mod.EventBusSubscriber(modid = Aether.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Aether {
    public static final String MODID = "aether";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Path DIRECTORY = FMLPaths.CONFIGDIR.get().resolve(Aether.MODID);

    public static final TriviaGenerator TRIVIA_READER = new TriviaGenerator();

    public Aether() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::curiosSetup);
        modEventBus.addListener(this::dataSetup);
        modEventBus.addListener(this::packSetup);

        DeferredRegister<?>[] registers = {
                AetherBlocks.BLOCKS,
                AetherItems.ITEMS,
                AetherEntityTypes.ENTITY_TYPES,
                AetherBlockEntityTypes.BLOCK_ENTITY_TYPES,
                AetherMenuTypes.MENU_TYPES,
                AetherEffects.EFFECTS,
                AetherParticleTypes.PARTICLES,
                AetherFeatures.FEATURES,
                AetherFoliagePlacerTypes.FOLIAGE_PLACERS,
                AetherTreeDecoratorTypes.TREE_DECORATORS,
                AetherPoi.POI,
                AetherStructureTypes.STRUCTURE_TYPES,
                AetherStructurePieceTypes.STRUCTURE_PIECE_TYPES,
                AetherStructureProcessors.STRUCTURE_PROCESSOR_TYPES,
                AetherRecipeTypes.RECIPE_TYPES,
                AetherRecipeSerializers.RECIPE_SERIALIZERS,
                AetherLootFunctions.LOOT_FUNCTION_TYPES,
                AetherLootConditions.LOOT_CONDITION_TYPES,
                AetherLootModifiers.GLOBAL_LOOT_MODIFIERS,
                AetherSoundEvents.SOUNDS,
                AetherGameEvents.GAME_EVENTS,
                AetherMoaTypes.MOA_TYPES,
                AetherSensorTypes.SENSOR_TYPES,
                AetherMemoryModuleTypes.MEMORY_TYPES
        };

        for (DeferredRegister<?> register : registers) {
            register.register(modEventBus);
        }

        AetherBlocks.registerWoodTypes(); // Registered this early to avoid bugs with WoodTypes and signs.

        DIRECTORY.toFile().mkdirs(); // Ensures the Aether's config folder is generated.
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AetherConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AetherConfig.CLIENT_SPEC);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        AetherPacketHandler.register();

        Reflection.initialize(SunAltarWhitelist.class);
        Reflection.initialize(AetherPlacementModifiers.class);
        Reflection.initialize(AetherRecipeBookTypes.class);
        Reflection.initialize(AetherBlockPathTypes.class);

        AetherAdvancementTriggers.init();

        this.registerFuels();

        event.enqueueWork(() -> {
            AetherBlocks.registerPots();
            AetherBlocks.registerFlammability();

            AetherItems.setupBucketReplacements();

            this.registerDispenserBehaviors();
            this.registerCauldronInteractions();
            this.registerComposting();
        });
    }

    public void curiosSetup(InterModEnqueueEvent event) {
        // All slots are marked with .hide() so they don't appear in the Curios GUI, as they are only added to the Aether's accessory menu which is done manually in its code.
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("aether_pendant").icon(new ResourceLocation(Aether.MODID, "gui/slots/pendant")).hide().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("aether_cape").icon(new ResourceLocation(Aether.MODID, "gui/slots/cape")).hide().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("aether_ring").icon(new ResourceLocation(Aether.MODID, "gui/slots/ring")).size(2).hide().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("aether_shield").icon(new ResourceLocation(Aether.MODID, "gui/slots/shield")).hide().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("aether_gloves").icon(new ResourceLocation(Aether.MODID, "gui/slots/gloves")).hide().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("aether_accessory").icon(new ResourceLocation(Aether.MODID, "gui/slots/misc")).size(2).hide().build());
    }

    public void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();

        // Client Data
        generator.addProvider(event.includeClient(), new AetherBlockStateData(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new AetherItemModelData(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new AetherLanguageData(packOutput));
        generator.addProvider(event.includeClient(), new AetherSoundData(packOutput, fileHelper));

        // Server Data
        generator.addProvider(event.includeServer(), new AetherWorldGenData(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new AetherRecipeData(packOutput));
        generator.addProvider(event.includeServer(), AetherLootTableData.create(packOutput));
        generator.addProvider(event.includeServer(), new AetherLootModifierData(packOutput));
        generator.addProvider(event.includeServer(), new AetherAdvancementData(packOutput, lookupProvider, fileHelper));
        AetherBlockTagData blockTags = new AetherBlockTagData(packOutput, lookupProvider, fileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new AetherItemTagData(packOutput, lookupProvider, blockTags, fileHelper));
        generator.addProvider(event.includeServer(), new AetherEntityTagData(packOutput, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new AetherFluidTagData(packOutput, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new AetherBiomeTagData(packOutput, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new AetherStructureTagData(packOutput, lookupProvider, fileHelper));
    }

    public void packSetup(AddPackFindersEvent event) {
        this.setupReleasePack(event);
        this.setupBetaPack(event);
        this.setupCTMFixPack(event);
    }

    /**
     * A built-in resource pack for programmer art based on the 1.2.5 version of the mod.
     */
    private void setupReleasePack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path resourcePath = ModList.get().getModFileById(Aether.MODID).getFile().findResource("packs/classic_125");
            PathPackResources pack = new PathPackResources(ModList.get().getModFileById(Aether.MODID).getFile().getFileName() + ":" + resourcePath, false, resourcePath);
            this.createCombinedPack(event, resourcePath, pack, "builtin/aether_125_art", "pack.aether.125.title", "pack.aether.125.description");
        }
    }

    /**
     * A built-in resource pack for programmer art based on the b1.7.3 version of the mod.
     */
    private void setupBetaPack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path resourcePath = ModList.get().getModFileById(Aether.MODID).getFile().findResource("packs/classic_b173");
            PathPackResources pack = new PathPackResources(ModList.get().getModFileById(Aether.MODID).getFile().getFileName() + ":" + resourcePath, false, resourcePath);
            this.createCombinedPack(event, resourcePath, pack, "builtin/aether_b173_art", "pack.aether.b173.title", "pack.aether.b173.description");
        }
    }

    /**
     * Creates a built-in resource pack that combines asset files from two different locations.
     * @param sourcePath The {@link Path} of the non-base assets.
     * @param pack The {@link PathPackResources} that handles the non-base asset path for the resource pack.
     * @param name The {@link String} internal name of the resource pack.
     * @param title The {@link String} title of the resource pack.
     * @param description The {@link String} description of the resource pack.
     */
    private void createCombinedPack(AddPackFindersEvent event, Path sourcePath, PathPackResources pack, String name, String title, String description) {
        Path baseResourcePath = ModList.get().getModFileById(Aether.MODID).getFile().findResource("packs/classic_base");
        PathPackResources basePack = new PathPackResources(ModList.get().getModFileById(Aether.MODID).getFile().getFileName() + ":" + baseResourcePath, false, baseResourcePath);
        List<PathPackResources> mergedPacks = List.of(pack, basePack);
        Pack.ResourcesSupplier resourcesSupplier = (string) -> new CombinedResourcePack(name, new PackMetadataSection(Component.translatable(description), PackType.CLIENT_RESOURCES.getVersion(SharedConstants.getCurrentVersion())), mergedPacks, sourcePath);
        Pack.Info info = Pack.readPackInfo(name, resourcesSupplier);
        if (info != null) {
            event.addRepositorySource((source) ->
                    source.accept(Pack.create(
                            name,
                            Component.translatable(title),
                            false,
                            resourcesSupplier,
                            info,
                            PackType.CLIENT_RESOURCES,
                            Pack.Position.TOP,
                            false,
                            PackSource.BUILT_IN)
                    ));
        }
    }

    /**
     * A built-in resource pack to change the model of Quicksoil Glass Panes when using CTM, as CTM's connected textures won't properly work with the normal Quicksoil Glass Pane model.<br><br>
     * The pack is loaded and automatically applied if CTM is installed.
     */
    private void setupCTMFixPack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES && ModList.get().isLoaded("ctm")) {
            Path resourcePath = ModList.get().getModFileById(Aether.MODID).getFile().findResource("packs/ctm_fix");
            PathPackResources pack = new PathPackResources(ModList.get().getModFileById(Aether.MODID).getFile().getFileName() + ":" + resourcePath, true, resourcePath);
            PackMetadataSection metadata = new PackMetadataSection(Component.translatable("pack.aether.ctm.description"), PackType.CLIENT_RESOURCES.getVersion(SharedConstants.getCurrentVersion()));
            event.addRepositorySource((source) ->
                    source.accept(Pack.create(
                        "builtin/aether_ctm_fix",
                            Component.translatable("pack.aether.ctm.title"),
                            true,
                            (string) -> pack,
                            new Pack.Info(metadata.getDescription(), metadata.getPackFormat(PackType.SERVER_DATA), metadata.getPackFormat(PackType.CLIENT_RESOURCES), FeatureFlagSet.of(), pack.isHidden()),
                            PackType.CLIENT_RESOURCES,
                            Pack.Position.TOP,
                            false,
                            PackSource.BUILT_IN)
                ));
        }
    }

    private void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(AetherItems.GOLDEN_DART.get(), new DispenseDartBehavior(AetherItems.GOLDEN_DART));
        DispenserBlock.registerBehavior(AetherItems.POISON_DART.get(), new DispenseDartBehavior(AetherItems.POISON_DART));
        DispenserBlock.registerBehavior(AetherItems.ENCHANTED_DART.get(), new DispenseDartBehavior(AetherItems.ENCHANTED_DART));
        DispenserBlock.registerBehavior(AetherItems.LIGHTNING_KNIFE.get(), AetherDispenseBehaviors.DISPENSE_LIGHTNING_KNIFE_BEHAVIOR);
        DispenserBlock.registerBehavior(AetherItems.HAMMER_OF_NOTCH.get(), AetherDispenseBehaviors.DISPENSE_NOTCH_HAMMER_BEHAVIOR);
        DispenserBlock.registerBehavior(AetherItems.SKYROOT_WATER_BUCKET.get(), AetherDispenseBehaviors.SKYROOT_BUCKET_DISPENSE_BEHAVIOR);
		DispenserBlock.registerBehavior(AetherItems.SKYROOT_BUCKET.get(), AetherDispenseBehaviors.SKYROOT_BUCKET_PICKUP_BEHAVIOR);
        DispenserBlock.registerBehavior(AetherItems.AMBROSIUM_SHARD.get(), new DispenseUsableItemBehavior<>(AetherRecipeTypes.AMBROSIUM_ENCHANTING.get()));
        DispenserBlock.registerBehavior(AetherItems.SWET_BALL.get(), new DispenseUsableItemBehavior<>(AetherRecipeTypes.SWET_BALL_CONVERSION.get()));
    }

    private void registerCauldronInteractions() {
        CauldronInteraction.EMPTY.put(AetherItems.SKYROOT_WATER_BUCKET.get(), AetherCauldronInteractions.FILL_WATER);
        CauldronInteraction.WATER.put(AetherItems.SKYROOT_WATER_BUCKET.get(), AetherCauldronInteractions.FILL_WATER);
        CauldronInteraction.LAVA.put(AetherItems.SKYROOT_WATER_BUCKET.get(), AetherCauldronInteractions.FILL_WATER);
        CauldronInteraction.POWDER_SNOW.put(AetherItems.SKYROOT_WATER_BUCKET.get(), AetherCauldronInteractions.FILL_WATER);
        CauldronInteraction.EMPTY.put(AetherItems.SKYROOT_POWDER_SNOW_BUCKET.get(), AetherCauldronInteractions.FILL_POWDER_SNOW);
        CauldronInteraction.WATER.put(AetherItems.SKYROOT_POWDER_SNOW_BUCKET.get(), AetherCauldronInteractions.FILL_POWDER_SNOW);
        CauldronInteraction.LAVA.put(AetherItems.SKYROOT_POWDER_SNOW_BUCKET.get(), AetherCauldronInteractions.FILL_POWDER_SNOW);
        CauldronInteraction.POWDER_SNOW.put(AetherItems.SKYROOT_POWDER_SNOW_BUCKET.get(), AetherCauldronInteractions.FILL_POWDER_SNOW);
        CauldronInteraction.WATER.put(AetherItems.SKYROOT_BUCKET.get(), AetherCauldronInteractions.EMPTY_WATER);
        CauldronInteraction.POWDER_SNOW.put(AetherItems.SKYROOT_BUCKET.get(), AetherCauldronInteractions.EMPTY_POWDER_SNOW);
        CauldronInteraction.WATER.put(AetherItems.LEATHER_GLOVES.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(AetherItems.RED_CAPE.get(), AetherCauldronInteractions.CAPE);
        CauldronInteraction.WATER.put(AetherItems.BLUE_CAPE.get(), AetherCauldronInteractions.CAPE);
        CauldronInteraction.WATER.put(AetherItems.YELLOW_CAPE.get(), AetherCauldronInteractions.CAPE);
    }

    private void registerComposting() {
        this.addCompost(0.3F, AetherBlocks.SKYROOT_LEAVES.get().asItem());
        this.addCompost(0.3F, AetherBlocks.SKYROOT_SAPLING.get());
        this.addCompost(0.3F, AetherBlocks.GOLDEN_OAK_LEAVES.get());
        this.addCompost(0.3F, AetherBlocks.GOLDEN_OAK_SAPLING.get());
        this.addCompost(0.3F, AetherBlocks.CRYSTAL_LEAVES.get());
        this.addCompost(0.3F, AetherBlocks.CRYSTAL_FRUIT_LEAVES.get());
        this.addCompost(0.3F, AetherBlocks.HOLIDAY_LEAVES.get());
        this.addCompost(0.3F, AetherBlocks.DECORATED_HOLIDAY_LEAVES.get());
        this.addCompost(0.3F, AetherItems.BLUE_BERRY.get());
        this.addCompost(0.5F, AetherItems.ENCHANTED_BERRY.get());
        this.addCompost(0.5F, AetherBlocks.BERRY_BUSH.get());
        this.addCompost(0.5F, AetherBlocks.BERRY_BUSH_STEM.get());
        this.addCompost(0.65F, AetherBlocks.WHITE_FLOWER.get());
        this.addCompost(0.65F, AetherBlocks.PURPLE_FLOWER.get());
        this.addCompost(0.65F, AetherItems.WHITE_APPLE.get());
    }

    /**
     * Copy of {@link ComposterBlock#add(float, ItemLike)}.
     * @param chance Chance (as a {@link Float}) to fill a compost layer.
     * @param item The {@link ItemLike} that can be composted.
     */
    private void addCompost(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    private void registerFuels() {
        AltarBlockEntity.addItemEnchantingTime(AetherItems.AMBROSIUM_SHARD.get(), 500);
        AltarBlockEntity.addItemEnchantingTime(AetherBlocks.AMBROSIUM_BLOCK.get(), 5000);
        FreezerBlockEntity.addItemFreezingTime(AetherBlocks.ICESTONE.get(), 500);
        FreezerBlockEntity.addItemFreezingTime(AetherBlocks.ICESTONE_SLAB.get(), 250);
        FreezerBlockEntity.addItemFreezingTime(AetherBlocks.ICESTONE_STAIRS.get(), 500);
        FreezerBlockEntity.addItemFreezingTime(AetherBlocks.ICESTONE_WALL.get(), 500);
        IncubatorBlockEntity.addItemIncubatingTime(AetherBlocks.AMBROSIUM_TORCH.get(), 1000);
    }
}
