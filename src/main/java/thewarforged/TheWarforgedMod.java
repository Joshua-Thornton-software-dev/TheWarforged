package thewarforged;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.Level;
import thewarforged.cards.BaseCard;
import thewarforged.character.TheWarforged;
import thewarforged.relics.BaseRelic;
import thewarforged.util.GeneralUtils;
import thewarforged.util.KeywordInfo;
import thewarforged.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class TheWarforgedMod implements
        EditCharactersSubscriber,
        EditRelicsSubscriber,
        EditCardsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new TheWarforgedMod();

        //Register the "colors" of the character's cards (not really colors, more like type categories).
        TheWarforged.Meta.registerColor();
    }

    public TheWarforgedMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        //This is a constant so that logger.info stops complaining.
        String SUBSCRIBED_INFO_STRING = modID + " subscribed to BaseMod.";
        logger.info(SUBSCRIBED_INFO_STRING);
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.

        //If you want to set up a config panel, that will be done here.
        //You can find information about this on the BaseMod wiki page "Mod Config and Panel".
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
//                e.printStackTrace();
                //Since printStackTrace is considered bad practice, I am logging the error this way instead.
                logger.log(Level.ERROR, e);
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                final String NOT_SUPPORTED_STRING = modID + " does not support " + getLangString() + " keywords.";
                logger.warn(NOT_SUPPORTED_STRING);
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     * <br> <br>
     * Suppressing the extract method recommendation because I am not familiar enough with all of this to risk
     * moving this around.
     */
    @SuppressWarnings("ExtractMethodRecommender")
    private static String checkResourcesPath() {
        //Using class.getPackage() can be iffy with patching, so class.getName() is used instead.
        String name = TheWarforgedMod.class.getName();
        int separator = name.indexOf('.');
        //IDEA Seems to think that "separator" will always be > 0,
        // so I unwrapped the following line from the if statement.
        name = name.substring(0, separator);

        //Once more familiar with all of this, extract this method and remove above suppression.
        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be named \"" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + TheWarforgedMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     * <br> <br>
     * Suppression necessary since IDEA seems to think that a map of <URL, AnnotationDB> can somehow
     * have a URL has a value instead of a key. Perhaps there is a better way to handle this, but I do not know
     * yet and I don't want this warning all the time.
     */
    @SuppressWarnings("UrlHashCode")
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(TheWarforgedMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    //This is necessary to implement the EditCharactersSubscriber interface.
    @Override
    public void receiveEditCharacters() {
        //Registers the character defined in java > thewarforged > character > TheWarforged.java.
        TheWarforged.Meta.registerCharacter();
    }

    //This is necessary to implement the EditCardsSubscriber interface.
    @Override
    public void receiveEditCards() {
        //AutoAdd() loads files from this mod. In this case, we are loading (registering?) cards.
        new AutoAdd(modID)
                //Ensures that only files in the same package as this mod are used (I believe).
                // I also believe that passing in BaseCard.class excludes the base card from being added,
                // since it is not really a card by itself.
                .packageFilter(BaseCard.class)
                //Sets the cards (maybe the files? Probably the cards) as being seen in the compendium.
                .setDefaultSeen(true)
                //Adds the cards (instead of relics or events, I think).
                .cards();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(BaseRelic.class)
                .any(BaseRelic.class, (info, relic) -> {
                    //relic.pool will NOT be null if this relic is character-specific. Because we already filtered
                    // to only relics in my package, this means that they will be custom and specific to the Warforged.
                    if (relic.pool != null) {
                        //Add that relic to the custom pool that is specified on the relic (Warforged custom pool).
                        BaseMod.addRelicToCustomPool(relic, relic.pool);
                    } else {
                        //These have null value for relic.pool, so they are generic relics (or specific to a base
                        // game character, IDK how that works).
                        BaseMod.addRelic(relic, relic.relicType);
                    }

                    //Relic class annotated with @AutoAdd.seen will have their info.seen marked true.
                    // (I'm sure that it can be set manually, not sure how that worked yet)
                    // This makes any relic that has been seen before visible in the relic library.
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }
}
