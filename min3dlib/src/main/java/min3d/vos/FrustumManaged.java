package min3d.vos;

import android.opengl.Matrix;

/**
 * 'Managed' VO for the view frustrum. Used by Camera.
 */
public class FrustumManaged
{
//	private float _shortSideLength;
//	private float _horizontalCenter;
//	private float _verticalCenter;
//	private float _zNear;
//	private float _zFar;

	private static final float DEFAULT_FOVY = 75.f;
	private static final float DEFAULT_NEAR = 0.01f;
	private static final float DEFAULT_FAR = 100.0f;

	private float[] mMatrix = new float[16];
	
	
	public FrustumManaged(int widthPixel, int heightPixel)
	{
		/*super($parent);
		
		_horizontalCenter = 0f;
		_verticalCenter = 0f;
		_shortSideLength = 1.0f;
		
		_zNear = 1.0f;
		_zFar = 100.0f;*/

		this(DEFAULT_FOVY, (float)widthPixel / (float)heightPixel, DEFAULT_NEAR, DEFAULT_FAR);

	}

	public FrustumManaged(float fovy, float aspect, float near, float far)
	{
		Matrix.perspectiveM(mMatrix, 0, fovy, aspect, near, far);
	}

	public FrustumManaged(float fovy, int widthPixel, int heightPixel, float near, float far)
	{
		Matrix.perspectiveM(mMatrix, 0, fovy, (float)widthPixel / (float)heightPixel, near, far);
	}

	public float[] getProjectionMatrix() {
		return mMatrix;
	}

}
