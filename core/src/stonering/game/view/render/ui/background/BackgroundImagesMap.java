package stonering.game.view.render.ui.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.util.global.FileLoader;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements lazy-loading for images.
 * Loads {@link ImageDescriptor} from regions.json on startup.
 * Creates {@link Image} from descriptors on demand.
 * Saves images in a map for faster access.
 */
public class BackgroundImagesMap {
    private static BackgroundImagesMap instance;
    private static Map<String, ImageDescriptor> descriptors;
    private static Map<String, Image> images;

    private BackgroundImagesMap() {
        descriptors = new HashMap<>();
        images = new HashMap<>();
        loadRegions();
    }

    public static BackgroundImagesMap getInstance() {
        if (instance == null) instance = new BackgroundImagesMap();
        return instance;
    }

    /**
     * Loads descriptors fo images.
     */
    private void loadRegions() {
        System.out.println("loading images");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<ImageDescriptor> elements = json.fromJson(ArrayList.class, ImageDescriptor.class, FileLoader.getFile(FileLoader.REGIONS_PATH));
        for (ImageDescriptor descriptor : elements) {
            descriptors.put(descriptor.name, descriptor);
        }
    }

    /**
     * Resolves {@link Image} by string key. Only images described in regions.json can be got.
     * After first creation of image object, saves it in a map.
     */
    public Image getBackground(String key) {
        if (images.containsKey(key)) return images.get(key); // image exists
        if (descriptors.containsKey(key)) { // create image
            ImageDescriptor descriptor = descriptors.get(key);
            Image image = new Image(new TextureRegion(new Texture("sprites/ui_back.png"), descriptor.x, descriptor.y, descriptor.width, descriptor.height));
            images.put(key, image);
            return images.get(key);
        }
        TagLoggersEnum.UI.logWarn("Image with key " + key + " not found");
        return null; // image not descripted
    }

    private static class ImageDescriptor {
        String name;
        int width;
        int height;
        int x;
        int y;
    }
}
