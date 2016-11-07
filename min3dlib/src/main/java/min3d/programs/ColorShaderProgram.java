package min3d.programs;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * Created by sw on 2016/11/3.
 */

public class ColorShaderProgram extends ShaderProgram {

    protected static final String U_MATRIX = "m_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String A_POSITION = "a_Position";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Uniform locations
    private final int uMatrixLocation; // u_Matrix
    private final int uColorLocation; // u_Color

    // Attribute locations
    private final int aPositionLocation; // a_Position

    public ColorShaderProgram(String vertexShaderScript, String fragmentShaderScript) {
        super(vertexShaderScript, fragmentShaderScript);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
    }

    public void setUniformMatrix(float[] mvpMatrix) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false,  mvpMatrix, 0);

    }

    public void setUniformColor(float r, float g, float b, float a) {
        GLES20.glUniform4f(uColorLocation, r, g, b, a);
    }

    public void setAttributeVectors(int stride, FloatBuffer buffer) {
        GLES20.glVertexAttribPointer(aPositionLocation, 3, GLES20.GL_FLOAT, false, stride, buffer);
    }
}
