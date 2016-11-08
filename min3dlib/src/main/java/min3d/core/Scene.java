package min3d.core;

import java.util.ArrayList;

import min3d.interfaces.IDirtyParent;
import min3d.interfaces.IObject3dContainer;
import min3d.interfaces.ISceneController;
import min3d.programs.ProgramFactory;
import min3d.vos.CameraVo;
import min3d.vos.Color4Managed;
import min3d.vos.FrustumManaged;


public class Scene implements IObject3dContainer, IDirtyParent {
    private ArrayList<Object3d> _children = new ArrayList<Object3d>();

    private CameraVo _camera;
    private FrustumManaged frustum;
    private Color4Managed _backgroundColor;
    private ISceneController _sceneController;
    private ProgramFactory bf;

    public Scene(ISceneController $sceneController) {
        _sceneController = $sceneController;
        bf = new ProgramFactory();
    }

    /**
     * Resets Scene to default settings.
     * Removes and clears any attached Object3ds.
     * Resets light list.
     */
    public void reset() {
        clearChildren(this);

        _children = new ArrayList<Object3d>();

        _camera = new CameraVo();

        _backgroundColor = new Color4Managed(0, 0, 0, 255, this);

    }

    public void setViewFrustum(int x, int y, int width, int height) {
        frustum = new FrustumManaged(width, height);
    }

    /**
     * Adds Object3d to Scene. Object3d's must be added to Scene in order to be rendered
     * Returns always true.
     */
    public void addChild(Object3d $o) {
        if (_children.contains($o)) return;

        _children.add($o);

        $o.parent(this);
        $o.scene(this);
    }

    public void addChildAt(Object3d $o, int $index) {
        if (_children.contains($o)) return;

        _children.add($index, $o);
    }

    /**
     * Removes Object3d from Scene.
     * Returns false if unsuccessful
     */
    public boolean removeChild(Object3d $o) {
        $o.parent(null);
        $o.scene(null);
        return _children.remove($o);
    }

    public Object3d removeChildAt(int $index) {
        Object3d o = _children.remove($index);

        if (o != null) {
            o.parent(null);
            o.scene(null);
        }
        return o;
    }

    public Object3d getChildAt(int $index) {
        return _children.get($index);
    }

    /**
     * TODO: Use better lookup
     */
    public Object3d getChildByName(String $name) {
        for (int i = 0; i < _children.size(); i++) {
            if (_children.get(0).name() == $name) return _children.get(0);
        }
        return null;
    }

    public int getChildIndexOf(Object3d $o) {
        return _children.indexOf($o);
    }

    public int numChildren() {
        return _children.size();
    }


    void init() /*package-private*/ {

        this.reset();

        _sceneController.initScene();
        _sceneController.getInitSceneHandler().post(_sceneController.getInitSceneRunnable());
    }

    void update() {
        _sceneController.updateScene();
        _sceneController.getUpdateSceneHandler().post(_sceneController.getUpdateSceneRunnable());
    }

    public void drawSetup() {
        for (int i = 0, size = _children.size(); i < size; i++) {
            Object3d o = _children.get(i);
            o.bindData();
        }
    }

    public void drawScene() {

        for (int i = 0, size = _children.size(); i < size; i++) {
            Object3d o = _children.get(i);

            o.draw();
        }
    }

    /**
     * Used by Renderer
     */
    ArrayList<Object3d> children() /*package-private*/ {
        return _children;
    }

    private void clearChildren(IObject3dContainer $c) {
        for (int i = $c.numChildren() - 1; i >= 0; i--) {
            Object3d o = $c.getChildAt(i);
            o.clear();

            if (o instanceof Object3dContainer) {
                clearChildren((Object3dContainer) o);
            }
        }
    }

    public void onDirty() {
        //
    }

    public ISceneController sceneController() {
        return _sceneController;
    }

    public void sceneController(ISceneController $sceneController) {
        _sceneController = $sceneController;
    }

    public CameraVo camera() {
        return _camera;
    }

    public void camera(CameraVo $camera) {
        _camera = $camera;
    }

    public FrustumManaged getFrustum() {
        return frustum;
    }

    public Color4Managed backgroundColor() {
        return _backgroundColor;
    }

    public ProgramFactory getProgramFactory() {
        return bf;
    }

}
