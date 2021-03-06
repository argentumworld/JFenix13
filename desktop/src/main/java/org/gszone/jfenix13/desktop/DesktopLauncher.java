package org.gszone.jfenix13.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import org.gszone.jfenix13.Main;
import org.gszone.jfenix13.general.DtConfig;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import static org.gszone.jfenix13.general.DtConfig.*;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {

        // Se define un trozo de código encargado de reiniciar el juego
        final Runnable rebootable = new Runnable() {
            @Override public void run() {
                if (Gdx.app != null) {
                    Gdx.app.exit();
                }
                start();
            }
        };

        // Se crea la aplicación
        createLwjglApplication(new Main(rebootable));
    }

    /**
     * Devuelve la aplicación ya lista, con todas sus configuraciones
     */
    private static Lwjgl3Application createLwjglApplication(Main main) {

        DtConfig.loadConfig();

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setDecorated(decorated);
        config.setWindowedMode(width, height);
        config.setResizable(resizable);
        config.setWindowIcon("icon128.png", "icon64.png", "icon32.png", "icon16.png");
        config.useVsync(vSync);
        config.setWindowSizeLimits(800, 600, 1200, 900);


        if (fullscreeen) {
            for (Graphics.DisplayMode dm : config.getDisplayModes()) {
                if (dm.width == width && dm.height == height) {
                    config.setFullscreenMode(dm);
                    break;
                }
            }
        }

        return new Lwjgl3Application(main, config);
    }

    /**
     * Inicia la aplicación nuevamente
     *
     * (inserta un nuevo comando, indicando de abrir el juego)
     */
    public static void start() {
        final StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (final String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp \"").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append("\" ");
        cmd.append(DesktopLauncher.class.getName()).append(" ");

        try {
            System.out.println(cmd.toString());
            Runtime.getRuntime().exec(cmd.toString());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}