package min3d.vos;

import android.opengl.Matrix;

/**
 * Encapsulates camera-related properties, including view frustrum.
 */
public class CameraVo {
    //public Number3d eye;
    //public Number3d target;
    //public Number3d up;

    private float[] mMatrix = new float[16];

    public CameraVo() {
        //Matrix.setLookAtM();
    }

    public CameraVo(Number3d eye, Number3d target, Number3d up) {
        Matrix.setLookAtM(mMatrix, 0, eye.x, eye.y, eye.z,
                target.x, target.y, target.z,
                up.x, up.y, up.z);
    }

    public CameraVo(float eyeX, float eyeY, float eyeZ,
                    float targetX, float targetY, float targetZ,
                    float upX, float upY, float upZ) {
        Matrix.setLookAtM(mMatrix, 0, eyeX, eyeY, eyeZ,
                targetX, targetY, targetZ,
                upX, upY, upZ);
    }

    public void setCameraVo(Number3d eye, Number3d target, Number3d up) {
        Matrix.setLookAtM(mMatrix, 0, eye.x, eye.y, eye.z,
                target.x, target.y, target.z,
                up.x, up.y, up.z);
    }

    public void setCameraVo(float eyeX, float eyeY, float eyeZ,
                    float targetX, float targetY, float targetZ,
                    float upX, float upY, float upZ) {
        Matrix.setLookAtM(mMatrix, 0, eyeX, eyeY, eyeZ,
                targetX, targetY, targetZ,
                upX, upY, upZ);
    }

    public void resetCamera() {
        Matrix.setIdentityM(mMatrix, 0);
    }


    public float[] getViewMatrix() {
        return mMatrix;
    }

    public void recycle() {
        mMatrix = null;
    }
}
