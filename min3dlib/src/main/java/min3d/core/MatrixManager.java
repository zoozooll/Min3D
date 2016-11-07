package min3d.core;

import android.opengl.Matrix;

import min3d.vos.Number3d;

/**
 * Created by sw on 2016/11/3.
 */

public class MatrixManager {


    private float[] viewMatrix = new float[16];

    private float[] projectionMatrix = new float[16];


    private MatrixManager () {
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);
    }

    public float[] setViewMatrix(Number3d eye, Number3d target, Number3d up) {
        Matrix.setLookAtM(viewMatrix, 0, eye.x, eye.y, eye.z,
                target.x, target.y, target.z,
                up.x, up.y, up.z);
        return viewMatrix;
    }

    public float[] setProgjectionMatrix (float fovy, float aspect, float zNear, float zFar) {
        Matrix.perspectiveM(projectionMatrix, 0, fovy, aspect, zNear, zFar);
        return projectionMatrix;
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }


}
