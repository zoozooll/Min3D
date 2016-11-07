package min3d.programs;

import java.util.WeakHashMap;

/**
 * Created by sw on 2016/11/3.
 */
public class ProgramFactory {

    public ProgramFactory() {
    }

    private WeakHashMap<String, ShaderProgram> programMap = new WeakHashMap<String, ShaderProgram>();

    public ShaderProgram getShaderProgram(String name) {
        ShaderProgram p = programMap.get(name);
        if (p != null) {
            return p;
        } else {
            p = instanceProgramWithName(name);
            if (p != null) {
                programMap.put(name, p);
            }
        }
        return p;
    }

    private static ShaderProgram instanceProgramWithName(String className) {

        String fullName;
        if (className.contains(".")) {
            fullName = className;
        } else {
            fullName = ShaderProgram.class.getPackage().getName() + "." + className;
        }
        ShaderProgram program = null;
        try {
            Class clazz = Class.forName(fullName);
            program = (ShaderProgram) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return program;
    }

}
