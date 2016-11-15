package min3d.core;

import android.opengl.Matrix;

import java.util.ArrayList;

import min3d.interfaces.IObject3dContainer;
import min3d.vos.Color4;
import min3d.vos.Number3d;
import min3d.vos.RenderType;

/**
 * @author Lee
 */
public class Object3d implements Cloneable {
    private String name;

    private RenderType renderType = RenderType.TRIANGLES;

    private boolean isVisible = true;
    private boolean vertexColorsEnabled = true;
    private boolean doubleSidedEnabled = false;
    private boolean texturesEnabled = true;
    private boolean normalsEnabled = true;
    private boolean ignoreFaces = false;
    private boolean colorMaterialEnabled = false;
    private boolean lightingEnabled = true;

    //private Number3d this.position = new Number3d(0, 0, 0);
    //private Number3d this.rotation = new Number3d(0, 0, 0);
    //private Number3d this.scale = new Number3d(1, 1, 1);

    private Color4 defaultColor = new Color4();

    private float pointSize = 3f;
    private boolean pointSmoothing = true;
    private float lineWidth = 1f;
    private boolean lineSmoothing = false;


    protected ArrayList<Object3d> children;

    protected Vertices vertices;
    protected TextureList textures;
    protected FacesBufferedList faces;

    protected boolean animationEnabled = false;

    private Scene scene;
    private IObject3dContainer parent;
    protected float[] modelMatrix = new float[16];

    /**
     * Maximum number of vertices and faces must be specified at instantiation.
     */
    public Object3d(int $maxVertices, int $maxFaces) {
        this.vertices = new Vertices($maxVertices, true, true, true);
        this.faces = new FacesBufferedList($maxFaces);
        this.textures = new TextureList();
        Matrix.setIdentityM(modelMatrix, 0);
    }

    /**
     * Adds three arguments
     */
    public Object3d(int $maxVertices, int $maxFaces, Boolean $useUvs, Boolean $useNormals, Boolean $useVertexColors) {
        this.vertices = new Vertices($maxVertices, $useUvs, $useNormals, $useVertexColors);
        this.faces = new FacesBufferedList($maxFaces);
        this.textures = new TextureList();
        Matrix.setIdentityM(modelMatrix, 0);
    }

    /**
     * This constructor is convenient for cloning purposes
     */
    public Object3d(Vertices $vertices, FacesBufferedList $faces, TextureList $textures) {
        this.vertices = $vertices;
        this.faces = $faces;
        this.textures = $textures;
        Matrix.setIdentityM(modelMatrix, 0);
    }

    /**
     * Clear object for garbage collection.
     */
    public void clear() {
        if (this.vertices().points() != null) this.vertices().points().clear();
        if (this.vertices().uvs() != null) this.vertices().uvs().clear();
        if (this.vertices().normals() != null) this.vertices().normals().clear();
        if (this.vertices().colors() != null) this.vertices().colors().clear();
        if (this.textures != null) this.textures.clear();

        if (this.parent() != null) this.parent().removeChild(this);
    }

    /**
     * Dind data to shader's attribute and uniform variables
     */
    public void bindData() {

    }

    /**
     * Call open gl to draw datas
     */
    public void draw() {

    }

    public Object3d clone() {
        Vertices v = this.vertices.clone();
        FacesBufferedList f = this.faces.clone();
        Object3d clone = new Object3d(v, f, this.textures);
        System.arraycopy(modelMatrix,0, clone.modelMatrix,0, modelMatrix.length);
        return clone;
    }

    /**
     * Holds references to vertex position list, vertex u/v mappings list, vertex normals list, and vertex colors list
     */
    public Vertices vertices() {
        return this.vertices;
    }

    /**
     * List of object's faces (ie, index buffer)
     */
    public FacesBufferedList faces() {
        return this.faces;
    }

    public TextureList textures() {
        return this.textures;
    }

    /**
     * Determines if object will be rendered.
     * Default is true.
     */
    public boolean isVisible() {
        return this.isVisible;
    }

    public void setVisible(Boolean $b) {
        this.isVisible = $b;
    }

    /**
     * Determines if backfaces will be rendered (ie, doublesided = true).
     * Default is false.
     */
    public boolean doubleSidedEnabled() {
        return this.doubleSidedEnabled;
    }

    public void doubleSidedEnabled(boolean $b) {
        this.doubleSidedEnabled = $b;
    }

    /**
     * Determines if object uses GLthis.COLORthis.MATERIAL or not.
     * Default is false.
     */
    public boolean colorMaterialEnabled() {
        return this.colorMaterialEnabled;
    }

    public boolean lightingEnabled() {
        return this.lightingEnabled;
    }

    public void lightingEnabled(boolean $b) {
        this.this.lightingEnabled = $b;
    }

    public void colorMaterialEnabled(boolean $b) {
        this.colorMaterialEnabled = $b;
    }

    /**
     * Determines whether animation is enabled or not. If it is enabled
     * then this should be an AnimationObject3d instance.
     * This is part of the Object3d class so there's no need to cast
     * anything during the render loop when it's not necessary.
     */
    public boolean animationEnabled() {
        return this.animationEnabled;
    }

    public void animationEnabled(boolean $b) {
        this.animationEnabled = $b;
    }

    /**
     * Determines if per-vertex colors will be using for rendering object.
     * If false, defaultColor property will dictate object color.
     * If object has no per-vertex color information, setting is ignored.
     * Default is true.
     */
    public boolean vertexColorsEnabled() {
        return this.vertexColorsEnabled;
    }

    public void vertexColorsEnabled(Boolean $b) {
        this.vertexColorsEnabled = $b;
    }

    /**
     * Determines if textures (if any) will used for rendering object.
     * Default is true.
     */
    public boolean texturesEnabled() {
        return this.texturesEnabled;
    }

    public void texturesEnabled(Boolean $b) {
        this.texturesEnabled = $b;
    }

    /**
     * Determines if object will be rendered using vertex light normals.
     * If false, no lighting is used on object for rendering.
     * Default is true.
     */
    public boolean normalsEnabled() {
        return this.normalsEnabled;
    }

    public void normalsEnabled(boolean $b) {
        this.normalsEnabled = $b;
    }

    /**
     * When true, Renderer draws using vertex points list, rather than faces list.
     * (ie, using glDrawArrays instead of glDrawElements)
     * Default is false.
     */
    public boolean ignoreFaces() {
        return this.ignoreFaces;
    }

    public void ignoreFaces(boolean $b) {
        this.ignoreFaces = $b;
    }

    /**
     * Options are: TRIANGLES, LINES, and POINTS
     * Default is TRIANGLES.
     */
    public RenderType renderType() {
        return this.renderType;
    }

    public void renderType(RenderType $type) {
        this.renderType = $type;
    }

    /**
     * Convenience 'pass-thru' method
     */
    public Number3dBufferList points() {
        return this.vertices.points();
    }

    /**
     * Convenience 'pass-thru' method
     */
    public UvBufferList uvs() {
        return this.vertices.uvs();
    }

    /**
     * Convenience 'pass-thru' method
     */
    public Number3dBufferList normals() {
        return this.vertices.normals();
    }

    /**
     * Convenience 'pass-thru' method
     */
    public Color4BufferList colors() {
        return this.vertices.colors();
    }

    /**
     * Convenience 'pass-thru' method
     */
    public boolean hasUvs() {
        return this.vertices.hasUvs();
    }

    /**
     * Convenience 'pass-thru' method
     */
    public boolean hasNormals() {
        return this.vertices.hasNormals();
    }

    public boolean hasVertexColors() {
        return this.vertices.hasColors();
    }


    public Color4 defaultColor() {
        return this.defaultColor;
    }

    public void defaultColor(Color4 color) {
        this.defaultColor = color;
    }

    /**
     * X/Y/Z position of object.
     */
    public void position(Number3d position) {
        position(position.x, position.y , position.z);
    }

    public void position(float x, float y, float z) {
        Matrix.translateM(modelMatrix, 0, x, y , z);
    }

    /**
     * X/Y/Z euler rotation of object, using Euler angles.
     * Units should be in degrees, to match OpenGL usage.
     */
    public void rotation(Number3d rotation) {
        rotation(rotation.x, rotation.y, rotation.z);
    }

    public void rotation(float x, float y, float z) {
        Matrix.rotateM(modelMatrix, 0,  90.f, x, y, z);
    }

    /**
     * X/Y/Z scale of object.
     */
    public void scale(Number3d scale) {
        scale(scale.x, scale.y, scale.z);
    }

    public void scale(float x, float y ,float z) {
        Matrix.scaleM(modelMatrix, 0, x, y, z);
    }

    /**
     * Point size (applicable when renderType is POINT)
     * Default is 3.
     */
    public float pointSize() {
        return this.pointSize;
    }

    public void pointSize(float $n) {
        this.pointSize = $n;
    }

    /**
     * Point smoothing (anti-aliasing), applicable when renderType is POINT.
     * When true, points look like circles rather than squares.
     * Default is true.
     */
    public boolean pointSmoothing() {
        return this.pointSmoothing;
    }

    public void pointSmoothing(boolean $b) {
        this.pointSmoothing = $b;
    }

    /**
     * Line width (applicable when renderType is LINE)
     * Default is 1.
     * <p>
     * Remember that maximum line width is OpenGL-implementation specific, and varies depending
     * on whether lineSmoothing is enabled or not. Eg, on Nexus One,  lineWidth can range from
     * 1 to 8 without smoothing, and can only be 1f with smoothing.
     */
    public float lineWidth() {
        return this.lineWidth;
    }

    public void lineWidth(float $n) {
        this.lineWidth = $n;
    }

    /**
     * Line smoothing (anti-aliasing), applicable when renderType is LINE
     * Default is false.
     */
    public boolean lineSmoothing() {
        return this.lineSmoothing;
    }

    public void lineSmoothing(boolean $b) {
        this.lineSmoothing = $b;
    }

    /**
     * Convenience property
     */
    public String name() {
        return this.name;
    }

    public void name(String $s) {
        this.name = $s;
    }

    public IObject3dContainer parent() {
        return this.parent;
    }

    public void parent(IObject3dContainer $container) /*package-private*/ {
        this.parent = $container;
    }

    /**
     * Called by Scene
     */
    public void scene(Scene $scene) /*package-private*/ {
        this.scene = $scene;
    }

    /**
     * Called by DisplayObjectContainer
     */
    public Scene scene() /*package-private*/ {
        return this.scene;
    }
}
