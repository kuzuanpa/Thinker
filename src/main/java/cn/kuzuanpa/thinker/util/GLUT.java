package cn.kuzuanpa.thinker.util;

import org.lwjgl.opengl.GL11;

public class GLUT {
    public static void glRotateCenterd(double angle, double x, double y, double z, int xPos, int yPos, int zPos, int width, int height, int length) {
        GL11.glTranslatef(xPos + (height / 2F), yPos + (width / 2F), zPos + (length / 2F));
        GL11.glRotated(angle, x, y, z);
        GL11.glTranslatef(-(xPos + (height / 2F)), -(yPos + (width / 2F)), -zPos + (length / 2F));
    }

    public static void glRotateCenterf(float angle, float x, float y, float z, int xPos, int yPos, int zPos, int width, int height, int length) {
        GL11.glTranslatef(xPos + (height / 2F), yPos + (width / 2F), zPos + (length / 2F));
        GL11.glRotatef(angle, x, y, z);
        GL11.glTranslatef(-(xPos + (height / 2F)), -(yPos + (width / 2F)), -zPos + (length / 2F));
    }

    public static void glScaleCenterd(double x, double y, double z, int xPos, int yPos, int zPos, int width, int height, int length) {
        GL11.glTranslatef(xPos + (height / 2F), yPos + (width / 2F), zPos + (length / 2F));
        GL11.glScaled(x, y, z);
        GL11.glTranslatef(-(xPos + (height / 2F)), -(yPos + (width / 2F)), -zPos + (length / 2F));
    }

    public static void glScaleCenterf(float x, float y, float z, int xPos, int yPos, int zPos, int width, int height, int length) {
        GL11.glTranslatef(xPos + (height / 2F), yPos + (width / 2F), zPos + (length / 2F));
        GL11.glScalef(x, y, z);
        GL11.glTranslatef(-(xPos + (height / 2F)), -(yPos + (width / 2F)), -zPos + (length / 2F));
    }

    public static void glBeginCenteredTransform(int xPos, int yPos, int zPos, int width, int height, int length) {
        GL11.glTranslatef(-(xPos + (height / 2F)), -(yPos + (width / 2F)), -zPos + (length / 2F));
    }
    public static void glEndCenteredTransform(int xPos, int yPos, int zPos, int width, int height, int length) {
        GL11.glTranslatef(xPos + (height / 2F), yPos + (width / 2F), zPos + (length / 2F));
    }
}
