package org.noear.solon.core;

import org.noear.solon.XApp;
import org.noear.solon.XUtil;
import org.noear.solon.ext.PrintUtil;

import java.io.File;

/**
 * 外部扩展加载器（对于动态扩展）
 *
 * @author noear
 * @since 1.0
 * */
public class ExtendLoader {
    private static final ExtendLoader _g = new ExtendLoader();
    private static String _path;

    /**
     * 扩展文件夹（绝对路径）
     * */
    public static String path(){
        return _path;
    }

    /**
     * 加载扩展文件夹（或文件）
     *
     * @param extPath 扩展路径
     * */
    public static void load(String extPath) {
        load(extPath,false);
    }

    /**
     * 加载扩展文件夹（或文件）
     *
     * @param extPath 扩展路径
     * @param autoMake 是否自动生成
     * */
    public static void load(String extPath, boolean autoMake) {
        if (XUtil.isNotEmpty(extPath)) {
            if (extPath.startsWith("!")) {
                extPath = extPath.substring(1);
                autoMake = true;
            }

            if (extPath.indexOf("/") < 0) {
                extPath = XUtil.buildExt(extPath, autoMake);
            }

            if (extPath != null) {
                _path = extPath;

                PrintUtil.blueln("solon.extend: " + extPath);

                File file = new File(extPath);
                _g.loadFile(file);
            }
        }
    }


    /**
     * 加载扩展具体的jar文件
     * */
    public static boolean loadJar(File file) {
        try {
            XClassLoader.global().loadJar(file.toURI().toURL());
            return true;
        }catch (Throwable ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 卸载一个已加载的jar文件
     * */
    public static boolean unloadJar(File file){
        try {
            XClassLoader.global().unloadJar(file.toURI().toURL());
            return true;
        }catch (Throwable ex){
            ex.printStackTrace();
            return false;
        }
    }

    private ExtendLoader() {

    }

    /**
     * 加载一个具体的文件
     *
     * 如果是目录的话，只处理一级
     * */
    private void loadFile(File file) {
        if (file.exists() == false) {
            return;
        }

        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loadFileDo(tmp);
            }
        } else {
            loadFileDo(file);
        }
    }


    /**
     * 加载一个具体的文件
     * */
    private void loadFileDo(File file) {
        if (file.isFile()) {
            String path = file.getAbsolutePath();
            try {
                //尝试加载jar包
                if (path.endsWith(".jar") || path.endsWith(".zip")) {
                    loadJar(file);
                    return;
                }

                //如果map不为null；尝试加载配置
                if (path.endsWith(".properties")) {
                    XApp.cfg().loadAdd(file.toURI().toURL());

                    PrintUtil.blueln("loaded: "+path);
                    return;
                }

                if (path.endsWith(".yml")) {
                    if (XPropertiesLoader.global.isSupport(path) == false) {
                        throw new RuntimeException("Do not support the *.yml");
                    }

                    XApp.cfg().loadAdd(file.toURI().toURL());

                    PrintUtil.blueln("loaded: " + path);
                    return;
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}
