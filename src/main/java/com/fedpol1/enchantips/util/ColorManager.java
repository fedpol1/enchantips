package com.fedpol1.enchantips.util;

import net.minecraft.text.TextColor;

public class ColorManager {

    public static float[] intToFloats(int inColor) {
        float[] outColor = new float[3];
        int r = (inColor & 0xff0000) >> 16;
        int g = (inColor & 0xff00) >> 8;
        int b = (inColor & 0xff);
        outColor[0] = r / 255.0f;
        outColor[1] = g / 255.0f;
        outColor[2] = b / 255.0f;
        return outColor;
    }

    public static TextColor lerpColor(TextColor min, TextColor max, float intensity) {
        int rgbMin = min.getRgb();
        int rgbMax = max.getRgb();

        int r1 = (rgbMin & 0xff0000) >> 16;
        int r2 = (rgbMax & 0xff0000) >> 16;
        int g1 = (rgbMin & 0xff00) >> 8;
        int g2 = (rgbMax & 0xff00) >> 8;
        int b1 = (rgbMin & 0xff);
        int b2 = (rgbMax & 0xff);

        r1 += (r2-r1) * intensity;
        g1 += (g2-g1) * intensity;
        b1 += (b2-b1) * intensity;

        return TextColor.fromRgb((r1<<16) + (g1<<8) + b1);
    }
}
