package min3d.programs;

import android.opengl.GLES20;

/**
 * Created by sw on 2016/10/26.
 */

public class ShaderProgram {

    protected final int program;

    public ShaderProgram(String vertexShaderScript, String fragmentShaderScript) {
        program = ShaderHelper.buildProgram(vertexShaderScript, fragmentShaderScript);
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }


}
