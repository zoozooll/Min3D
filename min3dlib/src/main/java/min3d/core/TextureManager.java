package min3d.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.HashMap;
import java.util.Set;

import min3d.Shared;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;

/**
 * TextureManager is responsible for managing textures for the whole environment.
 * It maintains a list of id's that are mapped to the GL texture names (id's).
 * <p>
 * You add a Bitmap to the TextureManager, which adds a textureId to its list.
 * Then, you assign one or more TextureVo's to your Object3d's using id's that
 * exist in the TextureManager.
 * <p>
 * Note that the _idToTextureName and _idToHasMipMap HashMaps used below
 * don't test for exceptions.
 */
public class TextureManager {
    private HashMap<String, Integer> _idToTextureName;
    private HashMap<String, Boolean> _idToHasMipMap;
    private static int _counter = 1000001;
    private static int _atlasId = 0;


    public TextureManager() {
        reset();
    }

    public void reset() {
        // Delete any extant textures

        if (_idToTextureName != null) {
            Set<String> s = _idToTextureName.keySet();
            Object[] a = s.toArray();
            for (int i = 0; i < a.length; i++) {
                int glId = getGlTextureId((String) a[i]);
                Shared.renderer().deleteTexture(glId);
            }
            // ...pain
        }

        _idToTextureName = new HashMap<String, Integer>();
        _idToHasMipMap = new HashMap<String, Boolean>();
    }

    /**
     * 'Uploads' a texture via OpenGL which is mapped to a textureId to the TextureManager,
     * which can subsequently be used to assign textures to Object3d's.
     *
     * @return The textureId as added to TextureManager, which is identical to $id
     */
    public String addTextureId(Bitmap $b, String $id, boolean $generateMipMap) {
        if (_idToTextureName.containsKey($id))
            throw new Error("Texture id \"" + $id + "\" already exists.");

        int glId = uploadTextureAndReturnId($b, $generateMipMap);

        String s = $id;
        _idToTextureName.put(s, glId);
        _idToHasMipMap.put(s, $generateMipMap);

        _counter++;

        // For debugging purposes (potentially adds a lot of chatter)
        // logContents();

        return s;
    }

    private int uploadTextureAndReturnId(Bitmap bitmap, boolean generateMipMap) /*package-private*/ {
        if (bitmap == null) {
            return 0;
        }
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;


        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // Set filtering: a default must be set, or the texture will be
        // black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        if (generateMipMap)
            glGenerateMipmap(GL_TEXTURE_2D);
        else {
            glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        }

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
//		bitmap.recycle();

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    /**
     * Alternate signature for "addTextureId", with MIP mapping set to false by default.
     * Kept for API backward-compatibility.
     */
    public String addTextureId(Bitmap $b, String $id) {
        return this.addTextureId($b, $id, false);
    }

    /**
     * 'Uploads' texture via OpenGL and returns an autoassigned textureId,
     * which can be used to assign textures to Object3d's.
     */
    public String createTextureId(Bitmap $b, boolean $generateMipMap) {
        return addTextureId($b, (_counter + ""), $generateMipMap);
    }

    /**
     * Deletes a textureId from the TextureManager,
     * and deletes the corresponding texture from the GPU
     */
    public void deleteTexture(String $textureId) {
        int glId = _idToTextureName.get($textureId);
        Shared.renderer().deleteTexture(glId);
        _idToTextureName.remove($textureId);
        _idToHasMipMap.remove($textureId);

        // logContents();

        //xxx needs error check
    }

    /**
     * Returns a String Array of textureId's in the TextureManager
     */
    public String[] getTextureIds() {
        Set<String> set = _idToTextureName.keySet();
        String[] a = new String[set.size()];
        set.toArray(a);
        return a;
    }

    /**
     * Used by Renderer
     */
    int getGlTextureId(String $textureId) /*package-private*/ {
        return _idToTextureName.get($textureId);
    }

    /**
     * Used by Renderer
     */
    boolean hasMipMap(String $textureId) /*package-private*/ {
        return _idToHasMipMap.get($textureId);
    }


    public boolean contains(String $textureId) {
        return _idToTextureName.containsKey($textureId);
    }


    private String arrayToString(String[] $a) {
        String s = "";
        for (int i = 0; i < $a.length; i++) {
            s += $a[i].toString() + " | ";
        }
        return s;
    }

    private void logContents() {
    }

    public String getNewAtlasId() {
        return "atlas".concat(Integer.toString(_atlasId++));
    }
}
