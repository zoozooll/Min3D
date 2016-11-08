package min3d.objectPrimitives;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import min3d.core.Number3dBufferList;
import min3d.core.Object3dContainer;
import min3d.core.Scene;
import min3d.programs.ColorShaderProgram;
import min3d.utils.RendererUtils;
import min3d.vos.Color4;
import min3d.vos.RenderType;

public class Rectangle extends Object3dContainer {

    private final static String TAG = "Rectangle";

    private ColorShaderProgram colorProgram;

    public Rectangle(float width, float height, int segsW, int segsH) {
        this(width, height, segsW, segsH, new Color4(255, 0, 0, 255));
    }

    public Rectangle(float width, float height, int segsW, int segsH, Color4 color) {
        super(4 * segsW * segsH, 2 * segsW * segsH);

        /*int row, col;

        float w = width / segsW;
        float h = height / segsH;

        float width5 = width / 2f;
        float height5 = height / 2f;

        // Add vertices

        for (row = 0; row <= segsH; row++) {
            for (col = 0; col <= segsW; col++) {
                this.vertices().addVertex(
                        (float) col * w - width5, (float) row * h - height5, 0f,
                        (float) col / (float) segsW, 1 - (float) row / (float) segsH,
                        0, 0, 1f,
                        color.r, color.g, color.b, color.a
                );
            }
        }

        // Add faces and indices

        int colspan = segsW + 1;

        for (row = 1; row <= segsH; row++) {
            for (col = 1; col <= segsW; col++) {
                int lr = row * colspan + col;
                int ll = lr - 1;
                int ur = lr - colspan;
                int ul = ur - 1;
                Utils.addQuad(this, ul, ur, lr, ll);
            }
        }*/
        float halfW = width * 0.5f, halfH = height * 0.5f;
        _vertices.addVertex(0.f, 0.f, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, color.r, color.g, color.b, color.a);
        _vertices.addVertex(-halfW, halfH, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, color.r, color.g, color.b, color.a);
        _vertices.addVertex(-halfW, -halfH, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, color.r, color.g, color.b, color.a);
        _vertices.addVertex(halfW, -halfH, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, color.r, color.g, color.b, color.a);
        _vertices.addVertex(halfW, halfH, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, color.r, color.g, color.b, color.a);
        _vertices.addVertex(-halfW, halfH, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, color.r, color.g, color.b, color.a);
        renderType(RenderType.TRIANGLE_FAN);
    }

    @Override
    public void scene(Scene scene) {
        super.scene(scene);
        colorProgram = (ColorShaderProgram) scene().getProgramFactory()
                .getShaderProgram("ColorShaderProgram");
    }

    @Override
    public void bindData() {
        super.bindData();
        colorProgram.useProgram();
        float[] projectionMatrix = scene().getFrustum().getProjectionMatrix();
        /*float[] viewMatrix = scene().camera().getViewMatrix();
        float[] vpMatrix = new float[16];
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);*/
        float[] mvp = new float[16];
        Matrix.multiplyMM(mvp, 0, projectionMatrix, 0, modelMatrix, 0);
        colorProgram.setUniformMatrix(mvp);
        Color4 color = defaultColor();
        colorProgram.setUniformColor((float)color.r / 255.f, (float)color.g / 255.f,
                (float)color.b / 255.f, (float)color.a / 255.f);
        Number3dBufferList points = _vertices.points();
        FloatBuffer buffer = points.buffer();
        buffer.position(0);
        colorProgram.setAttributeVectors(3 * 4, buffer);
    }

    @Override
    public void draw() {

        //Log.d(TAG, "draw");
        colorProgram.setEnableAtrirbutePosition(true);
       /* int pos, len;

        if (! faces().renderSubsetEnabled()) {
            pos = 0;
            len = faces().size();
        }
        else {
            pos = faces().renderSubsetStartIndex() * FacesBufferedList.PROPERTIES_PER_ELEMENT;
            len = faces().renderSubsetLength();
        }

        faces().buffer().position(pos);

        GLES20.glDrawElements(
                renderType().glValue(),
                len * FacesBufferedList.PROPERTIES_PER_ELEMENT,
                GLES20.GL_SHORT,
                faces().buffer());*/
        GLES20.glDrawArrays(renderType().glValue(), 0, 6);
        colorProgram.setEnableAtrirbutePosition(false);
        RendererUtils.checkGlError("glDrawElements");
        super.draw();

    }
}
