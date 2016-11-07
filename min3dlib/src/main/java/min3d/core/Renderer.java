package min3d.core;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import min3d.Shared;


public class Renderer implements GLSurfaceView.Renderer {
    public static final int NUMGLES20LIGHTS = 8;

    private Scene _scene;
    private TextureManager _textureManager;

    private float _surfaceAspectRatio;

    private IntBuffer _scratchIntBuffer;
    private FloatBuffer _scratchFloatBuffer;
    private boolean _scratchB;

    // stats-related
    public static final int FRAMERATE_SAMPLEINTERVAL_MS = 1000 * 3;
    private boolean _logFps = false;
    private long _frameCount = 0;
    private float _fps = 0;
    private long _timeLastSample;
    private ActivityManager _activityManager;
    private ActivityManager.MemoryInfo _memoryInfo;


    public Renderer(Scene $scene) {
        _scene = $scene;

        _scratchIntBuffer = IntBuffer.allocate(4);
        _scratchFloatBuffer = FloatBuffer.allocate(4);

        _textureManager = new TextureManager();
        Shared.textureManager(_textureManager);

        _activityManager = (ActivityManager) Shared.context().getSystemService(Context.ACTIVITY_SERVICE);
        _memoryInfo = new ActivityManager.MemoryInfo();
    }

    public void onSurfaceCreated(GL10 $gl, EGLConfig eglConfig) {
        RenderCaps.setRenderCaps($gl);

        reset();

        _scene.init();
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        _surfaceAspectRatio = (float) w / (float) h;

        GLES20.glViewport(0, 0, w, h);

        _scene.setViewFrustum(0, 0, w, h);
        //TODO load view matrix
    }

    public void onDrawFrame(GL10 gl) {
        // Update 'model'
        _scene.update();

        // Update 'view'
        _scene.drawSetup();
        _scene.drawScene();

        if (_logFps) doFps();
    }

    //

    /**
     * Returns last sampled framerate (logFps must be set to true)
     */
    public float fps() {
        return _fps;
    }

    /**
     * Return available system memory in bytes
     */
    public long availMem() {
        _activityManager.getMemoryInfo(_memoryInfo);
        return _memoryInfo.availMem;
    }

    /**
     * Used by TextureManager
     */
    void deleteTexture(int $glTextureId) /*package-private*/ {
        int[] a = new int[1];
        a[0] = $glTextureId;
        GLES20.glDeleteTextures(1, a, 0);
    }

    /**
     * If true, framerate and memory is periodically calculated and Log'ed,
     * and gettable thru fps()
     */
    public void logFps(boolean $b) {
        _logFps = $b;

        if (_logFps) { // init
            _timeLastSample = System.currentTimeMillis();
            _frameCount = 0;
        }
    }

    private void doFps() {
        _frameCount++;

        long now = System.currentTimeMillis();
        long delta = now - _timeLastSample;
        if (delta >= FRAMERATE_SAMPLEINTERVAL_MS) {
            _fps = _frameCount / (delta / 1000f);

            _activityManager.getMemoryInfo(_memoryInfo);
            _timeLastSample = now;
            _frameCount = 0;
        }
    }

    private void reset() {
        // Reset TextureManager
        Shared.textureManager().reset();

        // Do OpenGL settings which we are using as defaults, or which we will not be changing on-draw

        // Explicit depth settings
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glDepthRangef(0, 1f);
        GLES20.glDepthMask(true);

        // Alpha enabled
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // "Transparency is best implemented using glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        // with primitives sorted from farthest to nearest."

        // Texture
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST); // (OpenGL default is GL_NEAREST_MIPMAP)
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR); // (is OpenGL default)

        // CCW frontfaces only, by default
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        //
        // Scene object init only happens here, when we get GL for the first time
        //
    }
}
