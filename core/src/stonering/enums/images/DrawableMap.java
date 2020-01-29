package stonering.enums.images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements lazy-loading for {@link Drawable}s.
 * Loads {@link TextureRegionDescriptor}s from regions.json on startup.
 * Creates {@link TextureRegionDrawable}s from descriptors on demand.
 * Saves drawables in a maps by descriptor's name for faster access.
 * Works with non-tiled atlases.
 */
public class DrawableMap {
    private static DrawableMap instance;
    private static Map<String, TextureRegionDescriptor> descriptors;
    private static Map<String, Drawable> drawables;
    private static Map<String, Texture> textures;
    private static Map<String, IconDescriptor> icons;

    private DrawableMap() {
        descriptors = new HashMap<>();
        drawables = new HashMap<>();
        textures = new HashMap<>();
        icons = new HashMap<>();
        loadRegions();
        loadIconDescriptors();
    }

    public static DrawableMap instance() {
        if (instance == null) instance = new DrawableMap();
        return instance;
    }

    /**
     * Loads descriptors for drawables. Drawable can be obtained only by descriptor keys.
     */
    private void loadRegions() {
        Logger.LOADING.logDebug("loading drawables");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<TextureRegionDescriptor> elements = json.fromJson(ArrayList.class, TextureRegionDescriptor.class, FileLoader.getFile(FileLoader.REGIONS_PATH));
        for (TextureRegionDescriptor descriptor : elements) {
            descriptors.put(descriptor.name, descriptor);
        }
    }

    private void loadIconDescriptors() {
        Logger.LOADING.logDebug("loading icons");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<IconDescriptor> elements = json.fromJson(ArrayList.class, IconDescriptor.class, FileLoader.getFile(FileLoader.ICONS_PATH));
        for (IconDescriptor descriptor : elements) {
            icons.put(descriptor.name, descriptor);
        }
    }

    /**
     * Resolves {@link Image} by string key. Only drawables described in regions.json can be got.
     * After first creation of image object, saves it in a map.
     */
    public Drawable getDrawable(String key) {
        if (!descriptors.containsKey(key)) return getDrawable("default"); // default drawable for unknown name
        if (!drawables.containsKey(key)) { // first request for drawable, create
            TextureRegionDrawable drawable = new TextureRegionDrawable(getTextureRegion(descriptors.get(key)));
            drawables.put(key, drawable);
        }
        return drawables.get(key);
    }

    /**
     * Creates {@link TextureRegion} by {@link TextureRegionDescriptor}, using {@link Texture} cache map.
     */
    private TextureRegion getTextureRegion(TextureRegionDescriptor desc) {
        if (!textures.containsKey(desc.texture)) textures.put(desc.texture, new Texture("sprites/" + desc.texture));
        return new TextureRegion(textures.get(desc.texture), desc.bounds[0], desc.bounds[1], desc.bounds[2], desc.bounds[3]);
    }

    /**
     * Gets drawables from atlases, as {@link AtlasesEnum} is for drawing on {@link Batch} and can only return {@link TextureRegion}s.
     */
    public Drawable getTileAtlasDrawable(AtlasesEnum atlas, int x, int y) {
        return new TextureRegionDrawable(atlas.getBlockTile(x, y));
    }

    public Drawable getIconDrawable(String name) {
        if (!icons.containsKey(name)) {
            Logger.LOADING.logWarn("Icon [" + name + "] not found, check /core/assets/resources/ui_background/icons.json");
            return new TextureRegionDrawable(AtlasesEnum.icons.getBlockTile(0, 0));
        }
        IconDescriptor descriptor = icons.get(name);
        return new TextureRegionDrawable(AtlasesEnum.icons.getBlockTile(descriptor.atlasXY[0], descriptor.atlasXY[1]));
    }

    public Drawable getFileDrawable(String path) {
        textures.putIfAbsent(path, new Texture("sprites/" + path));
        Texture texture = textures.get(path);
        if(texture == null) {
            Logger.LOADING.logWarn("Texture [" + path + "] not found, check /core/assets/sprites");
            return getDrawable("default");
        }
        return new TextureRegionDrawable(texture);
    }
}
