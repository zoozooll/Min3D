package min3d.sampleProject1;

import android.os.Bundle;

import min3d.core.RendererActivity;
import min3d.objectPrimitives.Rectangle;
import min3d.vos.Color4;

public class RectangleActivity extends RendererActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initScene() {

        scene.backgroundColor().setAll(0, 0, 175, 255);

        Rectangle angle = new Rectangle(1.0f, 1.0f, 2, 2);
        angle.position(0, 0, -1.f);
        angle.defaultColor(new Color4(255, 255, 0, 255));
        angle.animationEnabled(false);
        angle.colorMaterialEnabled(false);
        angle.doubleSidedEnabled(false);
        angle.normalsEnabled(false);
        angle.texturesEnabled(false);
        scene.addChild(angle);
    }

    @Override
    public void updateScene() {

    }

}
